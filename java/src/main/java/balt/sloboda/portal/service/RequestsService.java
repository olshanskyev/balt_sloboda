package balt.sloboda.portal.service;

import balt.sloboda.portal.model.Role;
import balt.sloboda.portal.model.User;
import balt.sloboda.portal.model.request.Request;
import balt.sloboda.portal.model.request.RequestParam;
import balt.sloboda.portal.model.request.RequestStatus;
import balt.sloboda.portal.model.request.RequestType;
import balt.sloboda.portal.model.request.predefined.NewUserRequest;
import balt.sloboda.portal.model.request.type.NewUserRequestParams;
import balt.sloboda.portal.repository.DbRequestParamsRepository;
import balt.sloboda.portal.repository.DbRequestTypesRepository;
import balt.sloboda.portal.repository.DbRequestsRepository;
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
    private WebSecurityUtils webSecurityUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private User adminUser;

    @Autowired
    private NewUserRequest newUserRequest;

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

    public boolean requestTypeAlreadyExists(RequestType requestType){
        return getRequestTypeByName(requestType.getName()).isPresent();
    }

    public RequestType saveRequestType(RequestType requestType){
        return dbRequestTypesRepository.save(requestType);
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
        return getAllRequestByStatusAndType(RequestStatus.NEW, (newUserRequest.getName())).stream()
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


    public Request createNewUserRequest(NewUserRequestParams newUserRequestParams){
        Map<String, String> paramValues = newUserRequestParams.buildValuesMap();
        Optional<User> foundAdmin = userService.findByUserName(adminUser.getUserName());

        if (!foundAdmin.isPresent()){
            throw new DataIntegrityViolationException("missingAdminUser");
        }
        Request createdRequest = createRequest(newUserRequest.getName(), "Create New User", "User registration", paramValues, foundAdmin.get().getId(), foundAdmin.get().getId());
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
                .setLastModifiedBy(new User().setId(lastModifyBy))
                .setParamValues(paramValues)
                .setStatus(RequestStatus.NEW);

        return dbRequestsRepository.save(newRequest);
    }

    public boolean newUserRequestAlreadyExists(NewUserRequestParams newUserRequestParams) {
        Optional<Request> found = dbRequestsRepository.findByTypeName(newUserRequest.getName()).stream()
                .filter(item -> {
                    String userName = item.getParamValues().get("userName");
                    return userName != null && userName.equals(newUserRequestParams.getUserName());
                }).findFirst();
        return found.isPresent();
    }


    public void updateRequest(Request request) {
        dbRequestsRepository.save(request);
    }

    // ================================== requests ===============================

    // ================================== request params =========================
    public List<RequestParam> getParamsByRequestType(String requestTypeName) {
        return dbRequestParamsRepository.findByRequestTypeName(requestTypeName);
    }
    // ================================== request params =========================





}
