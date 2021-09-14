package balt.sloboda.portal.service;

import balt.sloboda.portal.model.*;
import balt.sloboda.portal.model.request.Request;
import balt.sloboda.portal.model.request.RequestParam;
import balt.sloboda.portal.model.request.RequestStatus;
import balt.sloboda.portal.model.request.RequestType;
import balt.sloboda.portal.model.request.predefined.NewUserRequestType;
import balt.sloboda.portal.model.request.type.NewUserRequestParams;
import balt.sloboda.portal.repository.*;
import balt.sloboda.portal.service.exceptions.AlreadyExistsException;
import balt.sloboda.portal.service.exceptions.NotFoundException;
import balt.sloboda.portal.utils.JwtTokenUtil;
import balt.sloboda.portal.utils.Transcriptor;
import balt.sloboda.portal.utils.WebSecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private DbCalendarSelectionRepository dbCalendarSelectionRepository;

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
        if (webSecurityUtils.isAdmin())
            return dbRequestTypesRepository.findAll();
        return dbRequestTypesRepository.findAll().stream().filter(this::isRequestTypeAvailableForUser).collect(Collectors.toList());
    }

    public boolean isRequestTypeAvailableForUser(RequestType requestType){
        if (webSecurityUtils.isAdmin())
            return true;
        return webSecurityUtils.authorizedUserHasAnyRole(requestType.getRoles());
    }

    public RequestType getRequestTypeByName(String requestTypeName) throws NotFoundException{
        return dbRequestTypesRepository.findByName(requestTypeName).stream().findFirst().orElseThrow(() -> new NotFoundException("requestTypeNotFound"));
    }

    private boolean requestTypeAlreadyExists(RequestType requestType){
        try {
            return getRequestTypeByName(requestType.getName()) != null;
        } catch (NotFoundException ignore) {
            return false;
        }
    }

    public RequestType saveRequestType(RequestType requestType) throws AlreadyExistsException {
        if (requestType.getName() == null || requestType.getName().isEmpty()) { // generate name from title
            requestType.setName(Transcriptor.transliterate(requestType.getTitle()));
        }

        if (requestType.getRoles() == null || requestType.getRoles().isEmpty()) {
            requestType.setRoles(new HashSet<>(Collections.singletonList(Role.ROLE_USER)));
        }

        if (requestTypeAlreadyExists(requestType)) {
            throw new AlreadyExistsException("requestTypeAlreadyExists");
        } else {
            return dbRequestTypesRepository.save(requestType);
        }
    }

    public RequestType getRequestTypeById(long id) throws NotFoundException {
        return dbRequestTypesRepository.findById(id).orElseThrow(() -> new NotFoundException("requestTypeNotFound"));
    }

    @Transactional
    public RequestType updateRequestType(long requestTypeId, RequestType requestType) throws NotFoundException {
        RequestType byId = dbRequestTypesRepository.findById(requestTypeId).orElseThrow(() -> new NotFoundException("requestTypeNotFound"));
        // 1. delete params by request type id
        deleteParamsByRequestTypId(requestTypeId);

        // 2. delete Calendar Selection Data by request type id
        if (byId.getCalendarSelection() != null && byId.getCalendarSelection().getId() != null){
            dbCalendarSelectionRepository.deleteById(byId.getCalendarSelection().getId());
        }

        // save new params, new calendar selection and new request type
        return dbRequestTypesRepository.save(requestType.setId(requestTypeId));
    }

    public void deleteRequestType(long id) {
        dbRequestTypesRepository.deleteById(id);
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

    public List<Request> getAllCurrentUserRequests(Optional<List<RequestStatus>> status){
        if (status.isPresent())
            return dbRequestsRepository.findByOwnerUserNameAndStatusIn(webSecurityUtils.getAuthorizedUserName(), status.get());
        else
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


    public Request createNewUserRequest(NewUserRequestParams newUserRequestParams) throws AlreadyExistsException, NotFoundException {
        if (newUserRequestAlreadyExists(newUserRequestParams)){
            throw new AlreadyExistsException("newUserRequestAlreadyExists");
        }
        Map<String, String> paramValues = newUserRequestParams.buildValuesMap();
        Optional<User> foundAdmin = userService.findByUserName(adminUser.getUserName());

        if (!foundAdmin.isPresent()){
            throw new DataIntegrityViolationException("missingAdminUser");
        }
        Request createdRequest = createRequest(
                newUserRequestType.getName(),
                "User registration",
                paramValues,
                null,
                foundAdmin.get().getId(),
                foundAdmin.get().getId());
        // send confirmation mail
        emailService.sendUserRegistrationRequestConfirmation(paramValues.get("userName"));
        return createdRequest;
    }

    public Request createRequest(Request request) throws NotFoundException {
        String requestTypeName;
        if (request.getType() != null) {
            if (request.getType().getName() != null && !request.getType().getName().isEmpty()) {
                //get name of request type if present
                requestTypeName = request.getType().getName();
            } else {
                // or else get form db by id
                requestTypeName = getRequestTypeById(request.getType().getId()).getName();
            }
        } else {
            throw new DataIntegrityViolationException("requestTypeIdOrRequestTypeNameShouldBePresent");
        }

        Optional<User> authorizedUser = userService.findByUserName(webSecurityUtils.getAuthorizedUserName());
        if (authorizedUser.isPresent()) {
            return createRequest(requestTypeName, request.getComment(), request.getParamValues(), request.getCalendarSelection(), authorizedUser.get().getId(), authorizedUser.get().getId());
        } else {
            throw new RuntimeException("Unauthorized");
        }
    }

    private Request createRequest(String requestTypeName,
                                  String comment,
                                  Map<String, String> paramValues,
                                  CalendarSelectionData calendarSelection,
                                  Long owner,
                                  Long lastModifyBy) throws NotFoundException {

        RequestType requestType = getRequestTypeByName(requestTypeName);
        List<String> missingParams = checkMandatoryParameters(paramValues, requestType);
        if (missingParams.size() > 0){
            throw new DataIntegrityViolationException("missingParameters");
        }

        Request newRequest = new Request()
                .setType(requestType)
                .setComment(comment)
                .setCalendarSelection(calendarSelection)
                .setOwner(new User().setId(owner))
                .setAssignedTo(new User().setId(requestType.getAssignTo().getId()))
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


    private Request saveRequest(Request request) {
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

    public Request acceptRequest(Long requestId) throws NotFoundException{
        Optional<Request> request = dbRequestsRepository.findById(requestId);
        if (request.isPresent()){
            if (request.get().getType().getName().equals(newUserRequestType.getName())){
                return acceptNewUserRequest(request.get());
            } else {
                request.get().setStatus(RequestStatus.ACCEPTED);
                return saveRequest(request.get());
            }
        } else {
            throw new NotFoundException("newUserRequestNotFound");
        }
    }
    // ================================== requests ===============================


    // ================================== request params =========================
    public List<RequestParam> getParamsByRequestType(String requestTypeName) {
        return dbRequestParamsRepository.findByRequestTypeName(requestTypeName);
    }

    private void deleteParamsByRequestTypId(long requestTypeId){
        dbRequestParamsRepository.deleteByRequestTypeId(requestTypeId);
    }
    // ================================== request params =========================





}
