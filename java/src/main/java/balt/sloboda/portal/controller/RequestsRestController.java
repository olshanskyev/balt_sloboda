package balt.sloboda.portal.controller;

import balt.sloboda.portal.model.ErrorResponse;
import balt.sloboda.portal.model.request.Request;
import balt.sloboda.portal.model.request.RequestAction;
import balt.sloboda.portal.model.request.RequestStatus;
import balt.sloboda.portal.model.request.RequestType;
import balt.sloboda.portal.service.RequestsService;
import balt.sloboda.portal.service.exceptions.AlreadyExistsException;
import balt.sloboda.portal.service.exceptions.NotFoundException;
import balt.sloboda.portal.service.exceptions.RequestLifecycleException;
import balt.sloboda.portal.service.exceptions.UserNotAuthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class RequestsRestController {

    @Autowired
    private RequestsService requestsService;


    @RequestMapping(value="/management/requests", method = RequestMethod.GET)
    public ResponseEntity<?> getAllRequests(@RequestParam(name = "requestType") Optional<String> requestType,
                                            @RequestParam(name = "status") Optional<RequestStatus> status,
                                            @RequestParam("page") int page,
                                            @RequestParam("size") int size) {
        if (requestType.isPresent()){
            if (status.isPresent()){
                return new ResponseEntity<>(requestsService.getAllRequestByStatusAndType(status.get(), requestType.get(), page, size), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(requestsService.getAllRequestByType(requestType.get(), page, size), HttpStatus.OK);
            }
        } else {
            if (status.isPresent()){
                return new ResponseEntity<>(requestsService.getAllRequestByStatus(status.get(), page, size), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(requestsService.getAllRequests(page, size), HttpStatus.OK);
            }
        }
    }


    @RequestMapping(value="/requests", method = RequestMethod.POST)
    public ResponseEntity<?> createRequest(@RequestBody Request request) {
        try {
            return new ResponseEntity<>(requestsService.createRequest(request), HttpStatus.OK);
        } catch (NotFoundException notFoundException) {
            return new ResponseEntity<>(new ErrorResponse(notFoundException.getMessage(), null), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value="/requests/{requestId}/{action}", method = RequestMethod.PUT)
    public ResponseEntity<?> changeRequestStatus(@PathVariable Long requestId, @PathVariable RequestAction action,
                                                 @RequestParam(name = "comment") Optional<String> comment) {

        try {
            switch (action) {
                case accept: {
                    return new ResponseEntity<>(requestsService.acceptRequest(requestId), HttpStatus.OK);
                }
                case reject: {
                    return new ResponseEntity<>(requestsService.rejectRequest(requestId, comment), HttpStatus.OK);
                }
                default: {
                    return new ResponseEntity<>(new ErrorResponse("unknowRequestOperation", null), HttpStatus.BAD_REQUEST);
                }
            }

        } catch (NotFoundException ex){
            return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (UserNotAuthorizedException ex) {
            return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), null), HttpStatus.UNAUTHORIZED);
        } catch (RequestLifecycleException ex) {
            return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), new HashMap<String, String>() {{
                put("from", ex.getFrom().toString());
                put("to", ex.getTo().toString());
            }}), HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(value="/requestsCount", method = RequestMethod.GET)
    public ResponseEntity<?> getRequestsCount(   @RequestParam(name = "status") Optional<List<RequestStatus>> status,
                                                 @RequestParam(name = "assignedToMe") Optional<Boolean> assignedToMe
                                                ) {
        if (assignedToMe.isPresent() && assignedToMe.get()) // get only assigned to me requests
            return new ResponseEntity<>(requestsService.getAssignedToCurrentUserRequestsCount(status), HttpStatus.OK);
        else
            return new ResponseEntity<>(requestsService.getCurrentUserRequestsCount(status), HttpStatus.OK);

    }


    @RequestMapping(value="/requests", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUserRequests( @RequestParam(name = "status") Optional<List<RequestStatus>> status,
                                                 @RequestParam(name = "assignedToMe") Optional<Boolean> assignedToMe,
                                                 @RequestParam(name = "requestTypeName") Optional<String> requestTypeName,
                                                 @RequestParam("page") int page,
                                                 @RequestParam("size") int size) {
        if (assignedToMe.isPresent() && assignedToMe.get()) // get only assigned to me requests
            return new ResponseEntity<>(requestsService.getAllAssignedToCurrentUserRequests(status, requestTypeName, page, size), HttpStatus.OK);
        else
            return new ResponseEntity<>(requestsService.getAllCurrentUserRequests(status, requestTypeName, page, size), HttpStatus.OK);
    }

    @RequestMapping(value="/requestLogs", method = RequestMethod.GET)
    public ResponseEntity<?> getRequestLog(@RequestParam(name = "requestId") Long requestId) {
        try {
            return new ResponseEntity<>(requestsService.getAllRequestLogItem(requestId), HttpStatus.OK);
        } catch (NotFoundException ex){
            return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (UserNotAuthorizedException ex) {
            return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), null), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value="/management/requestTypes", method = RequestMethod.POST)
    public ResponseEntity<?> createRequestType(@RequestBody RequestType requestType) {

        try {
            return new ResponseEntity<>( requestsService.saveRequestType(requestType), HttpStatus.OK);
        } catch (AlreadyExistsException existsExeption) {
            return new ResponseEntity<>(new ErrorResponse(existsExeption.getMessage(), null), HttpStatus.CONFLICT);
        }

    }

    @RequestMapping(value="/requestTypes", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUserRequestTypes() {
        return new ResponseEntity<>(requestsService.getRequestTypesAvailableForUser(), HttpStatus.OK);
    }

    @RequestMapping(value="/requestTypes/{name}", method = RequestMethod.GET)
    public ResponseEntity<?> getUserRequestTypeByName(@PathVariable String name) {
        try {
            RequestType requestTypeByName = requestsService.getRequestTypeByName(name);
            if (requestsService.isRequestTypeAvailableForUser(requestTypeByName) && !requestTypeByName.isSystemRequest()) {
                return new ResponseEntity<>(requestTypeByName, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ErrorResponse("requestTypeNotAvailableForUser", new HashMap<String, String>() {{
                    put("requestType",name);
                }}), HttpStatus.CONFLICT);
            }

        } catch (NotFoundException notFoundException) {
            return new ResponseEntity<>(new ErrorResponse(notFoundException.getMessage(), new HashMap<String, String>() {{
                put("requestType",name);
            }}), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value="/management/requestTypes", method = RequestMethod.GET)
    public ResponseEntity<?> getAllRequestTypes() {
        return new ResponseEntity<>(requestsService.getAllRequestTypes(), HttpStatus.OK);
    }

    @RequestMapping(value="/management/requestTypes/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getRequestTypeById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(requestsService.getRequestTypeById(id), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage(), null), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value="/management/requestTypes/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteRequestType(@PathVariable Long id) {
        requestsService.deleteRequestType(id);
        return new ResponseEntity(HttpStatus.OK); // ToDo catch exception if not found
    }

    @RequestMapping(value="/management/requestTypes/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateRequestType(@PathVariable Long id, @RequestBody RequestType requestType) {
        try {
            return new ResponseEntity<>(requestsService.updateRequestType(id, requestType), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage(), null), HttpStatus.NOT_FOUND);
        }
    }
}
