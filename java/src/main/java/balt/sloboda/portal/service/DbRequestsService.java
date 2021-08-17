package balt.sloboda.portal.service;

import balt.sloboda.portal.model.Role;
import balt.sloboda.portal.model.User;
import balt.sloboda.portal.model.request.Request;
import balt.sloboda.portal.model.request.RequestParam;
import balt.sloboda.portal.model.request.RequestStatus;
import balt.sloboda.portal.model.request.RequestType;
import balt.sloboda.portal.model.request.predefined.NewUserRequest;
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
public class DbRequestsService {

    @Autowired
    private DbRequestsRepository dbRequestsRepository;

    @Autowired
    private DbRequestTypesRepository dbRequestTypesRepository;

    @Autowired
    private DbRequestParamsRepository dbRequestParamsRepository;

    @Autowired
    private WebSecurityUtils webSecurityUtils;

    @Autowired
    private DbUserService dbUserService;

    @Autowired
    private User adminUser;

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

    public List<Request> getAllRequests(){
        return dbRequestsRepository.findAll();
    }
    public List<Request> getAllCurrentUserRequests(){
        return dbRequestsRepository.findByOwnerUser(webSecurityUtils.getAuthorizedUserName());
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


    public Request createNewUserRequest(User user){
        Map<String, String> paramValues = new HashMap<String, String>(){{
            put("user", user.getUser());
            put("firstName", user.getFirstName());
            put("lastName", user.getLastName());
            put("street", user.getAddress().getStreet());
            put("houseNumber", String.valueOf(user.getAddress().getHouseNumber()));
            put("plotNumber", String.valueOf(user.getAddress().getPlotNumber()));
        }} ;
        // ToDo send mail
        Optional<User> foundAdmin = dbUserService.findByUserName(adminUser.getUser());

        if (!foundAdmin.isPresent()){
            throw new DataIntegrityViolationException("missingAdminUser");
        }

        return createRequest((new NewUserRequest()).getName(), "Create New User", "User registration", paramValues, foundAdmin.get().getId(), foundAdmin.get().getId());
    }

    public Request createRequest(String requestTypeName,
                                 String subject,
                                 String comment,
                                 Map<String, String> paramValues) {
        Optional<User> authorizedUser = dbUserService.findByUserName(webSecurityUtils.getAuthorizedUserName());
        if (authorizedUser.isPresent()) {
            return createRequest(requestTypeName, subject, comment, paramValues, authorizedUser.get().getId(), authorizedUser.get().getId());
        } else {
            throw new RuntimeException("unauthorized");
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
    // ================================== requests ===============================

    // ================================== request params =========================
    public List<RequestParam> getParamsByRequestType(String requestTypeName) {
        return dbRequestParamsRepository.findByRequestTypeName(requestTypeName);
    }
    // ================================== request params =========================





}
