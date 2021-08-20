package balt.sloboda.portal.controller;

import balt.sloboda.portal.model.User;
import balt.sloboda.portal.service.DbAddressService;
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
public class AddressesRestController {

    @Autowired
    private DbAddressService dbAddressService;
    
    @RequestMapping(value="/streets", method = RequestMethod.GET)
    public ResponseEntity<?> getAllStreets() {
        return new ResponseEntity<>(dbAddressService.getAllStreets(), HttpStatus.OK);
    }

    @RequestMapping(value="/addresses", method = RequestMethod.GET)
    public ResponseEntity<?> getAllAddresses() {
        return new ResponseEntity<>(dbAddressService.getAll(), HttpStatus.OK);
    }

    @RequestMapping(value="/streets/{street}/addresses", method = RequestMethod.GET)
    public ResponseEntity<?> getAddressesOnStreet(@PathVariable("street") String street) {
        return new ResponseEntity<>(dbAddressService.getAddressesOnStreet(street), HttpStatus.OK);
    }
}
