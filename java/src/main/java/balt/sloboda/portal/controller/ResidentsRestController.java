package balt.sloboda.portal.controller;

import balt.sloboda.portal.service.ResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

}
