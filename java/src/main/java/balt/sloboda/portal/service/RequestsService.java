package balt.sloboda.portal.service;

import balt.sloboda.portal.model.Address;
import balt.sloboda.portal.model.Resident;
import balt.sloboda.portal.model.Role;
import balt.sloboda.portal.model.User;
import balt.sloboda.portal.model.request.Request;
import balt.sloboda.portal.model.request.RequestParam;
import balt.sloboda.portal.model.request.RequestStatus;
import balt.sloboda.portal.model.request.RequestType;
import balt.sloboda.portal.model.request.predefined.NewUserRequestType;
import balt.sloboda.portal.model.request.type.NewUserRequestParams;
import balt.sloboda.portal.repository.*;
import balt.sloboda.portal.service.exceptions.AlreadyExistsExeption;
import balt.sloboda.portal.utils.JwtTokenUtil;
import balt.sloboda.portal.utils.Transcriptor;
import balt.sloboda.portal.utils.WebSecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RequestsService {

    @Autowired
    private DbRequestsRepository dbRequestsRepository;

    @Autowired
    private DbRequestTypesRepository dbRequestTypesRepository;

    @Autowired
    private DbRequestParamsRepository dbRequestParamsRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ResidentService residentService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private WebSecurityUtils webSecurityUtils;

    @Autowired
    private EmailService emailService;

    @Autowired
    private User adminUser;

    @Autowired
    private NewUserRequestType newUserRequestType;

    // ================================== request types===========================
    public List<RequestType> getAllRequestTypes() {
        return dbRequestTypesRepository.findAll();
    }

    public List<RequestType> getRequestTypesAvailableForUser() {
        return dbRequestTypesRepository.findAll().stream().filter(item -> item.getRoles().contains(Role.ROLE_USER)).collect(Collectors.toList());
    }

    public Optional<RequestType> getRequestTypeByName(String requestTypeName){
        return dbRequestTypesRepository.findByName(requestTypeName).stream().findFirst();
    }

    private boolean requestTypeAlreadyExists(RequestType requestType){
        return getRequestTypeByName(requestType.getName()).isPresent();
    }

    public RequestType saveRequestType(RequestType requestType) throws AlreadyExistsExeption {
        if (requestType.getName() == null || requestType.getName().isEmpty()) { // generate name from title
            requestType.setName(Transcriptor.transliterate(requestType.getTitle())/*.replace(" ", "")*/);
        }

        if (requestType.getRoles() == null || requestType.getRoles().isEmpty()) {
            requestType.setRoles(new HashSet<>(Collections.singletonList(Role.ROLE_USER)));
        }

        if (requestTypeAlreadyExists(requestType)) {
            throw new AlreadyExistsExeption("requestTypeAlreadyExists");
        } else {
            return dbRequestTypesRepository.save(requestType);
        }
    }

    // ================================== request types===========================

    // ================================== requests ===============================
    public List<Request> getAllRequestByType(String requestTypeName){
        return dbRequestsRepository.findByTypeName(requestTypeName);
    }

    public List<Request> getAllRequestByStatus(RequestStatus status){
        return dbRequestsRepository.findByStatus(status);
    }

    public List<Request> getAllRequestByStatusAndType(RequestStatus status, String requestTypeName){
        return dbRequestsRepository.findByStatusAndTypeName(status, requestTypeName);
    }

    public List<Request> getAllRequests(){
        return dbRequestsRepository.findAll();
    }

    public Optional<Request> getNewUserRequestByUser(String userName){
        return getAllRequestByStatusAndType(RequestStatus.NEW, (newUserRequestType.getName())).stream()
        .filter(item -> item.getParamValues().get("userName") != null && item.getParamValues().get("userName").equals(userName)).findFirst();
    }

    public List<Request> getAllCurrentUserRequests(){
        return dbRequestsRepository.findByOwnerUserName(webSecurityUtils.getAuthorizedUserName());
    }

    private List<String> checkMandatoryParameters(Map<String, String> paramValues, RequestType requestType) {
        List<String> missingParameters = new ArrayList<>();
        requestType.getParameters().forEach(item -> {
            if (!item.isOptional() && !paramValues.containsKey(item.getName())){
                missingParameters.add(item.getName());
            }
        });
        return missingParameters;
    }


    public Request createNewUserRequest(NewUserRequestParams newUserRequestParams) throws AlreadyExistsExeption {
        if (newUserRequestAlreadyExists(newUserRequestParams)){
            throw new AlreadyExistsExeption("newUserRequestAlreadyExists");
        }
        Map<String, String> paramValues = newUserRequestParams.buildValuesMap();
        Optional<User> foundAdmin = userService.findByUserName(adminUser.getUserName());

        if (!foundAdmin.isPresent()){
            throw new DataIntegrityViolationException("missingAdminUser");
        }
        Request createdRequest = createRequest(newUserRequestType.getName(), "Create New User", "User registration", paramValues, foundAdmin.get().getId(), foundAdmin.get().getId());
        // send confirmation mail
        emailService.sendUserRegistrationRequestConfirmation(paramValues.get("userName"));
        return createdRequest;
    }

    public Request createRequest(String requestTypeName,
                                 String subject,
                                 String comment,
                                 Map<String, String> paramValues) {
        Optional<User> authorizedUser = userService.findByUserName(webSecurityUtils.getAuthorizedUserName());
        if (authorizedUser.isPresent()) {
            return createRequest(requestTypeName, subject, comment, paramValues, authorizedUser.get().getId(), authorizedUser.get().getId());
        } else {
            throw new RuntimeException("Unauthorized");
        }
    }

    private Request createRequest(String requestTypeName,
                                  String subject,
                                  String comment,
                                  Map<String, String> paramValues,
                                  Long owner,
                                  Long lastModifyBy) {
        Optional<RequestType> requestType = getRequestTypeByName(requestTypeName);
        if (!requestType.isPresent()){
            throw new DataIntegrityViolationException("requestTypeNotFound");
        }
        List<String> missingParams = checkMandatoryParameters(paramValues, requestType.get());
        if (missingParams.size() > 0){
            throw new DataIntegrityViolationException("missingParameters");
        }

        Request newRequest = new Request()
                .setType(requestType.get())
                .setSubject(subject)
                .setComment(comment)
                .setOwner(new User().setId(owner))
                .setAssignedTo(new User().setId(owner))
                .setLastModifiedBy(new User().setId(lastModifyBy))
                .setParamValues(paramValues)
                .setStatus(RequestStatus.NEW);

        return dbRequestsRepository.save(newRequest);
    }

    private boolean newUserRequestAlreadyExists(NewUserRequestParams newUserRequestParams) {
        Optional<Request> found = dbRequestsRepository.findByTypeName(newUserRequestType.getName()).stream()
                .filter(item -> {
                    String userName = item.getParamValues().get("userName");
                    return userName != null && userName.equals(newUserRequestParams.getUserName()) && item.getStatus() == RequestStatus.NEW;
                }).findFirst();
        return found.isPresent();
    }


    public Request saveRequest(Request request) {
       return dbRequestsRepository.save(request);
    }

    private Request acceptNewUserRequest(Request request){ //save all objects in db, update request status, send email with link
        String userName = request.getParamValues().get("userName");
        String token = jwtTokenUtil.generatePasswordResetToken(userName);
        User user = userService.createUser(new User()
                .setUserName(userName)
                .setRoles(new HashSet<>(Arrays.asList(Role.ROLE_USER)))
                .setPassword("")
                .setPasswordResetToken(token));

        Optional<Address> address = addressService.getAddressByAddress(new Address()
                .setStreet(request.getParamValues().get("street"))
                .setHouseNumber(Integer.parseInt(request.getParamValues().get("houseNumber")))
                .setPlotNumber(Integer.parseInt(request.getParamValues().get("plotNumber")))
        );
        if (!address.isPresent()){
            throw new RuntimeException("addressNotFound");
        }
        residentService.save(new Resident()
                .setFirstName(request.getParamValues().get("firstName"))
                .setLastName(request.getParamValues().get("lastName"))
                .setUser(user)
                .setAddress(address.get()));
        request.setStatus(RequestStatus.CLOSED);
        Request saved = saveRequest(request);
        emailService.sendNewUserPasswordResetLink(userName, token);
        return saved;
    }

    public Request acceptRequest(Long requestId) {
        Optional<Request> request = dbRequestsRepository.findById(requestId);
        if (request.isPresent()){
            if (request.get().getType().getName().equals(newUserRequestType.getName())){
                return acceptNewUserRequest(request.get());
            } else {
                request.get().setStatus(RequestStatus.ACCEPTED);
                return saveRequest(request.get());
            }
        } else {
            throw new RuntimeException("newUserRequestNotFound");
        }
    }
    // ================================== requests ===============================


    // ================================== request params =========================
    public List<RequestParam> getParamsByRequestType(String requestTypeName) {
        return dbRequestParamsRepository.findByRequestTypeName(requestTypeName);
    }
    // ================================== request params =========================





}
