package balt.sloboda.portal.controller;

import balt.sloboda.portal.model.ErrorResponse;
import balt.sloboda.portal.model.Resident;
import balt.sloboda.portal.model.User;
import balt.sloboda.portal.service.DbResidentService;
import balt.sloboda.portal.service.DbUserService;
import balt.sloboda.portal.utils.WebSecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class ResidentsRestController {

    @Autowired
    private DbResidentService dbResidentService;

    @RequestMapping(value="/management/residents", method = RequestMethod.GET)
    public ResponseEntity<?> getAllResidents(){
        return new ResponseEntity<>(dbResidentService.selectAllResidents(), HttpStatus.OK);
    }

}
