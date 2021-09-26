package balt.sloboda.portal.service;

import balt.sloboda.portal.model.*;
import balt.sloboda.portal.model.request.*;
import balt.sloboda.portal.model.request.predefined.NewUserRequestType;
import balt.sloboda.portal.model.request.type.NewUserRequestParams;
import balt.sloboda.portal.repository.*;
import balt.sloboda.portal.service.exceptions.AlreadyExistsException;
import balt.sloboda.portal.service.exceptions.RequestLifecycleException;
import balt.sloboda.portal.service.exceptions.UserNotAuthorizedException;
import balt.sloboda.portal.service.exceptions.NotFoundException;
import balt.sloboda.portal.utils.JwtTokenUtil;
import balt.sloboda.portal.utils.RequestLifecycleUtil;
import balt.sloboda.portal.utils.Transcriptor;
import balt.sloboda.portal.utils.WebSecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private DbRequestsLogRepository dbRequestsLogRepository;

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

    @Autowired
    private RequestLifecycleUtil requestLifecycleUtil;

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
        // 3 letters prefix
        if (requestType.getRequestIdPrefix() == null || requestType.getRequestIdPrefix().isEmpty()){
            String name = requestType.getName();
            String prefix =
                    String.valueOf(name.charAt(0)) + // first letter
                            name.charAt(name.length() / 2) + //middle letter
                            name.charAt(name.length() - 1); // last letter
            requestType.setRequestIdPrefix(prefix.toUpperCase());
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

    private Sort requestsSort() {
        return Sort.by("lastModifiedDate").descending();
    }

    public Page<Request> getAllRequestByType(String requestTypeName, int page, int size){
        return dbRequestsRepository.findByTypeName(requestTypeName, PageRequest.of(page, size, requestsSort()));
    }

    public Page<Request> getAllRequestByStatus(RequestStatus status, int page, int size){
        return dbRequestsRepository.findByStatus(status, PageRequest.of(page, size, requestsSort()));
    }

    public Page<Request> getAllRequestByStatusAndType(RequestStatus status, String requestTypeName, int page, int size){
        return dbRequestsRepository.findByStatusAndTypeName(status, requestTypeName, PageRequest.of(page, size, requestsSort()));
    }

    public Page<Request> getAllRequests(int page, int size){
        return dbRequestsRepository.findAll(PageRequest.of(page, size));
    }

    // =============== current user/assigned to current user requests ========================
    public Page<Request> getAllAssignedToCurrentUserRequests(Optional<List<RequestStatus>> status, Optional<String> typeName, int page, int size){
        if (status.isPresent()) {
            if (typeName.isPresent() && typeName.get().length() > 0) {
                return dbRequestsRepository.findByAssignedToUserNameAndTypeNameAndStatusInAndTypeSystemRequest(
                        webSecurityUtils.getAuthorizedUser().getUserName(), typeName.get(), status.get(), false, PageRequest.of(page, size, requestsSort()));
            } else {
                return dbRequestsRepository.findByAssignedToUserNameAndStatusInAndTypeSystemRequest(webSecurityUtils.getAuthorizedUser().getUserName(), status.get(), false, PageRequest.of(page, size, requestsSort()));
            }
        }
        else{
            if (typeName.isPresent() && typeName.get().length() > 0) {
                return dbRequestsRepository.findByAssignedToUserNameAndTypeNameAndTypeSystemRequest(
                        webSecurityUtils.getAuthorizedUser().getUserName(), typeName.get(), false, PageRequest.of(page, size, requestsSort()));
            } else {
                return dbRequestsRepository.findByAssignedToUserNameAndTypeSystemRequest(webSecurityUtils.getAuthorizedUser().getUserName(), false, PageRequest.of(page, size, requestsSort()));
            }
        }

    }


    public Page<Request> getAllCurrentUserRequests(Optional<List<RequestStatus>> status, Optional<String> typeName, int page, int size){
        if (status.isPresent()) {
            if (typeName.isPresent() && typeName.get().length() > 0) {
                return dbRequestsRepository.findByOwnerUserNameAndTypeNameAndStatusInAndTypeSystemRequest(
                        webSecurityUtils.getAuthorizedUser().getUserName(), typeName.get(), status.get(), false, PageRequest.of(page, size, requestsSort()));
            } else {
                return dbRequestsRepository.findByOwnerUserNameAndStatusInAndTypeSystemRequest(webSecurityUtils.getAuthorizedUser().getUserName(), status.get(), false, PageRequest.of(page, size, requestsSort()));
            }
        }

        else {
            if (typeName.isPresent() && typeName.get().length() > 0) {
                return dbRequestsRepository.findByOwnerUserNameAndTypeNameAndTypeSystemRequest(
                        webSecurityUtils.getAuthorizedUser().getUserName(), typeName.get(), false, PageRequest.of(page, size, requestsSort()));
            } else {
                return dbRequestsRepository.findByOwnerUserNameAndTypeSystemRequest(webSecurityUtils.getAuthorizedUser().getUserName(), false, PageRequest.of(page, size, requestsSort()));
            }

        }
    }

    // =================== Requests Count ===================
    public List<IRequestsCount> getAllRequestsCount() {
        return dbRequestsRepository.findAllRequestsCount();
    }

    public List<IRequestsCount> getCurrentUserRequestsCount(Optional<List<RequestStatus>> status) {
        if (status.isPresent()){
            return dbRequestsRepository.findRequestsCountByOwnerAndStatus(webSecurityUtils.getAuthorizedUser().getUserName(), status.get());
        }
        else {
            return dbRequestsRepository.findRequestsCountByOwner(webSecurityUtils.getAuthorizedUser().getUserName());
        }
    }

    public List<IRequestsCount> getAssignedToCurrentUserRequestsCount(Optional<List<RequestStatus>> status) {
        if (status.isPresent()){
            return dbRequestsRepository.findRequestsCountByAssignedToAndStatus(webSecurityUtils.getAuthorizedUser().getUserName(), status.get());
        }
        else {
            return dbRequestsRepository.findRequestsCountByAssignedTo(webSecurityUtils.getAuthorizedUser().getUserName());
        }

    }

    /*List<IRequestsCount> getAssignedToCurrentUserRequestsCount() {
        return dbRequestsRepository.findRequestsCountGroupByType();
    }*/

    // ============================= create/save/modify =========================

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
                foundAdmin.get());
        // send confirmation mail
        emailService.sendUserRegistrationRequestConfirmation(paramValues.get("userName"));
        return createdRequest;
    }

    /*
    create request by user
     */
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

        User authorizedUser = webSecurityUtils.getAuthorizedUser();
        if (authorizedUser != null) {
            return createRequest(requestTypeName, request.getComment(), request.getParamValues(), request.getCalendarSelection(), authorizedUser);
        } else {
            throw new RuntimeException("Unauthorized");
        }
    }

    private Request createRequest(String requestTypeName,
                                  String comment,
                                  Map<String, String> paramValues,
                                  CalendarSelectionData calendarSelection,
                                  User creatingBy) throws NotFoundException {

        RequestType requestType = getRequestTypeByName(requestTypeName);
        List<String> missingParams = checkMandatoryParameters(paramValues, requestType);
        if (missingParams.size() > 0){
            throw new DataIntegrityViolationException("missingParameters");
        }

        Request newRequest = new Request()
                .setType(requestType)
                .setComment(comment)
                .setCalendarSelection(calendarSelection)
                .setOwner(creatingBy)
                .setAssignedTo(requestType.getAssignTo())
                .setParamValues(paramValues)
                .setStatus(RequestStatus.NEW);
        List<RequestLogItem> logs = Arrays.asList(
                new RequestLogItem()
                .setItemName(RequestLogItemName.STATUS_CHANGED).setNewValue(RequestStatus.NEW.toString()),
                new RequestLogItem()
                .setItemName(RequestLogItemName.ASSIGNED_TO_CHANGED).setNewValue(requestType.getAssignTo().getUserName()));
        return saveRequest(newRequest, logs);
    }

    private boolean newUserRequestAlreadyExists(NewUserRequestParams newUserRequestParams) {
        Optional<Request> found = dbRequestsRepository.findByTypeName(newUserRequestType.getName(), Pageable.unpaged()).stream()
                .filter(item -> {
                    String userName = item.getParamValues().get("userName");
                    return userName != null && userName.equals(newUserRequestParams.getUserName()) && item.getStatus() == RequestStatus.NEW;
                }).findFirst();
        return found.isPresent();
    }

    @Transactional
    private Request saveRequest(Request request, List<RequestLogItem> requestLogList) {
        User authorizedUser = webSecurityUtils.getAuthorizedUser();
        request.setLastModifiedBy(authorizedUser);
        Request savedRequest = dbRequestsRepository.save(request);
        requestLogList.forEach(item -> {
            item.setRequest(savedRequest);
            item.setModifiedBy(authorizedUser);
        });
        dbRequestsLogRepository.saveAll(requestLogList);
        return savedRequest;
    }

    @Transactional
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
        List<RequestLogItem> logItems = Arrays.asList(
                new RequestLogItem()
                        .setItemName(RequestLogItemName.STATUS_CHANGED).setNewValue(RequestStatus.CLOSED.toString()));

        Request saved = saveRequest(request, logItems);
        emailService.sendNewUserPasswordResetLink(userName, token);
        return saved;
    }


    private Request getRequestAndCheckAuthorization(Long requestId) throws UserNotAuthorizedException, NotFoundException {
        Optional<Request> request = dbRequestsRepository.findById(requestId);
        if (request.isPresent()){
            if (!webSecurityUtils.isAdmin()) { // not admin
                if(!request.get().getOwner().getUserName().equals(webSecurityUtils.getAuthorizedUser().getUserName())) { // not owner
                    throw new UserNotAuthorizedException("notOwner");
                }
            }
            return request.get();
        } else {
            throw new NotFoundException("requestNotFound");
        }
    }

    public Request rejectRequest(Long requestId, Optional<String> comment) throws NotFoundException, UserNotAuthorizedException, RequestLifecycleException {
        Request request = getRequestAndCheckAuthorization(requestId);
        requestLifecycleUtil.checkStatusChanging(request.getStatus(), RequestStatus.REJECTED);
        request.setStatus(RequestStatus.REJECTED);
        List<RequestLogItem> logItems = new ArrayList<>();
        comment.ifPresent(c -> logItems.add(new RequestLogItem()
                .setItemName(RequestLogItemName.COMMENT_ADDED).setNewValue(c)));
        logItems.add(new RequestLogItem()
                        .setItemName(RequestLogItemName.STATUS_CHANGED).setNewValue(RequestStatus.REJECTED.toString()));
        return saveRequest(request, logItems);
    }

    public Request acceptRequest(Long requestId) throws NotFoundException, UserNotAuthorizedException, RequestLifecycleException {
        Request request = getRequestAndCheckAuthorization(requestId);
        requestLifecycleUtil.checkStatusChanging(request.getStatus(), RequestStatus.ACCEPTED);
            if (request.getType().getName().equals(newUserRequestType.getName())){
                return acceptNewUserRequest(request);
            } else {
                request.setStatus(RequestStatus.ACCEPTED);
                List<RequestLogItem> logItems = Arrays.asList(
                        new RequestLogItem()
                                .setItemName(RequestLogItemName.STATUS_CHANGED).setNewValue(RequestStatus.ACCEPTED.toString()));
                return saveRequest(request, logItems);
            }

    }
    // ================================== requests ===============================

    // ================================== request logs ===========================
    public List<RequestLogItem> getAllRequestLogItem(Long requestId) throws UserNotAuthorizedException, NotFoundException {
        Request request = getRequestAndCheckAuthorization(requestId);
        return dbRequestsLogRepository.findByRequestId(request.getId());
    }
    // ================================== request logs ===========================

    // ================================== request params =========================
    public List<RequestParam> getParamsByRequestType(String requestTypeName) {
        return dbRequestParamsRepository.findByRequestTypeName(requestTypeName);
    }

    private void deleteParamsByRequestTypId(long requestTypeId){
        dbRequestParamsRepository.deleteByRequestTypeId(requestTypeId);
    }
    // ================================== request params =========================





}
