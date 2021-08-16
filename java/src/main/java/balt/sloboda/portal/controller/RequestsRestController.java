package balt.sloboda.portal.controller;

import balt.sloboda.portal.model.User;
import balt.sloboda.portal.service.DbRequestsService;
import balt.sloboda.portal.service.DbUserService;
import balt.sloboda.portal.utils.WebSecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class RequestsRestController {

    @Autowired
    private DbRequestsService dbRequestsService;


    @RequestMapping(value="/management/requests", method = RequestMethod.GET)
    public ResponseEntity<?> getAllRequests() {
        return new ResponseEntity<>(dbRequestsService.getAllRequests(), HttpStatus.OK);
    }

    @RequestMapping(value="/requests", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUserRequests() {
        return new ResponseEntity<>(dbRequestsService.getAllCurrentUserRequests(), HttpStatus.OK);
    }
}
