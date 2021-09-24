package balt.sloboda.portal.controller;

import balt.sloboda.portal.model.ErrorResponse;
import balt.sloboda.portal.service.ResidentService;
import balt.sloboda.portal.service.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ResidentsRestController {

    @Autowired
    private ResidentService residentService;

    @RequestMapping(value="/management/residents", method = RequestMethod.GET)
    public ResponseEntity<?> getAllResidents(){
        return new ResponseEntity<>(residentService.selectAllResidents(), HttpStatus.OK);
    }

    /*
    can return null f.e. for admin user
     */
    @RequestMapping(value="/management/residents/{userName}", method = RequestMethod.GET)
    public ResponseEntity<?> getResidentById(@PathVariable String userName) {
        return new ResponseEntity<>(residentService.getResidentByUserName(userName), HttpStatus.OK);
    }





}
