package balt.sloboda.portal.controller;

import balt.sloboda.portal.model.ErrorResponse;
import balt.sloboda.portal.model.request.RequestStatus;
import balt.sloboda.portal.service.RequestsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @RequestMapping(value="/management/requests/{requestId}/accept", method = RequestMethod.PUT)
    public ResponseEntity<?> acceptRequest(@PathVariable Long requestId) {
        try {
            return new ResponseEntity<>(requestsService.acceptRequest(requestId), HttpStatus.OK);
        } catch (RuntimeException ex){
            return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), null), HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value="/requests", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUserRequests() {
        return new ResponseEntity<>(requestsService.getAllCurrentUserRequests(), HttpStatus.OK);
    }
}
