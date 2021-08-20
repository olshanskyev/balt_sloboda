package balt.sloboda.portal.controller;

import balt.sloboda.portal.service.AddressService;
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
    private AddressService addressService;
    
    @RequestMapping(value="/streets", method = RequestMethod.GET)
    public ResponseEntity<?> getAllStreets() {
        return new ResponseEntity<>(addressService.getAllStreets(), HttpStatus.OK);
    }

    @RequestMapping(value="/addresses", method = RequestMethod.GET)
    public ResponseEntity<?> getAllAddresses() {
        return new ResponseEntity<>(addressService.getAll(), HttpStatus.OK);
    }

    @RequestMapping(value="/streets/{street}/addresses", method = RequestMethod.GET)
    public ResponseEntity<?> getAddressesOnStreet(@PathVariable("street") String street) {
        return new ResponseEntity<>(addressService.getAddressesOnStreet(street), HttpStatus.OK);
    }
}
