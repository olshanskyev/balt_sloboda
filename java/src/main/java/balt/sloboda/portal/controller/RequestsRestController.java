package balt.sloboda.portal.controller;

import balt.sloboda.portal.model.ErrorResponse;
import balt.sloboda.portal.model.request.Request;
import balt.sloboda.portal.model.request.RequestStatus;
import balt.sloboda.portal.model.request.RequestType;
import balt.sloboda.portal.service.RequestsService;
import balt.sloboda.portal.service.exceptions.AlreadyExistsException;
import balt.sloboda.portal.service.exceptions.NotFoundException;
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
                                            @RequestParam(name = "status") Optional<RequestStatus> status) {
        if (requestType.isPresent()){
            if (status.isPresent()){
                return new ResponseEntity<>(requestsService.getAllRequestByStatusAndType(status.get(), requestType.get()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(requestsService.getAllRequestByType(requestType.get()), HttpStatus.OK);
            }
        } else {
            if (status.isPresent()){
                return new ResponseEntity<>(requestsService.getAllRequestByStatus(status.get()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(requestsService.getAllRequests(), HttpStatus.OK);
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


    @RequestMapping(value="/management/requests/{requestId}/accept", method = RequestMethod.PUT)
    public ResponseEntity<?> acceptRequest(@PathVariable Long requestId) {
        try {
            return new ResponseEntity<>(requestsService.acceptRequest(requestId), HttpStatus.OK);
        } catch (NotFoundException ex){
            return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), null), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value="/requests", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUserRequests( @RequestParam(name = "status") Optional<RequestStatus> status) {
        return new ResponseEntity<>(requestsService.getAllCurrentUserRequests(status), HttpStatus.OK);
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
