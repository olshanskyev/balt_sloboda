package balt.sloboda.portal.controller;

import balt.sloboda.portal.model.*;
import balt.sloboda.portal.model.request.Request;
import balt.sloboda.portal.service.DbAddressService;
import balt.sloboda.portal.service.DbRequestsService;
import balt.sloboda.portal.service.DbResidentService;
import balt.sloboda.portal.service.DbUserService;
import balt.sloboda.portal.utils.JwtTokenUtil;
import balt.sloboda.portal.utils.TokenRefreshException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthRestController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DbUserService dbUserService;

    @Autowired
    private DbAddressService dbAddressService;

    @Autowired
    private DbRequestsService dbRequestsService;

    @Autowired
    private DbResidentService dbResidentService;

    @Autowired
    private JwtTokenUtil tokenUtil;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody JwtRequest authenticationRequest) throws Exception {

        //authenticate via email and password using authenticationManager
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
            JwtResponse jwtResponse = tokenUtil.generateTokens(authentication);
            return ResponseEntity.ok(jwtResponse);
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ErrorResponse("badCredentials"), HttpStatus.UNAUTHORIZED);
        }
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody Resident resident) {
        if (dbUserService.alreadyExists(resident.getUser())){
            return new ResponseEntity<>(new ErrorResponse("userAlreadyExists", new HashMap<String, String>() {{
                put("user", resident.getUser().getUserName());
            }}), HttpStatus.CONFLICT);
        }

        if (resident.getAddress() != null && resident.getAddress().getId() != null) {
            Optional<Address> addressById = dbAddressService.getAddressById(resident.getAddress().getId());
            if (addressById.isPresent()){
                if (dbResidentService.addressAlreadyUsed(addressById.get().getId())) { //address used by another user
                    return new ResponseEntity<>(new ErrorResponse("addressAlreadyUsed", null), HttpStatus.CONFLICT);
                }
                resident.setAddress(addressById.get());
            } else {
                // address not found
                return new ResponseEntity<>(new ErrorResponse("notExistingAddress", null), HttpStatus.CONFLICT);
            }
        } else {
            return new ResponseEntity<>(new ErrorResponse("notExistingAddress", null), HttpStatus.CONFLICT);
        }

        try {
            if (dbRequestsService.newUserRequestAlreadyExists(resident)) {
                return new ResponseEntity<>(new ErrorResponse("newUserRequestAlreadyExists", new HashMap<String, String>() {{
                    put("user", resident.getUser().getUserName());
                }}), HttpStatus.CONFLICT);
            }
            dbRequestsService.createNewUserRequest(resident);
            JwtResponse jwtResponse = new JwtResponse((new TokenPair()).setAccessToken("").setRefreshToken("")); // empty jwtResponse
            return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value = "/refresh-token", method = RequestMethod.POST)
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        try {
            return ResponseEntity.ok(tokenUtil.refreshTokens(refreshTokenRequest));
        } catch (TokenRefreshException ex) {
            return new ResponseEntity<>("Token refresh Error", HttpStatus.UNAUTHORIZED);
        }

    }

    @RequestMapping(value = "/logout", method = RequestMethod.DELETE)
    public ResponseEntity<?> logout() throws Exception {
        tokenUtil.deleteTokens();
        return ResponseEntity.ok("");
    }
}
