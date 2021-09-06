package balt.sloboda.portal.controller;

import balt.sloboda.portal.model.*;
import balt.sloboda.portal.model.request.type.NewUserRequestParams;
import balt.sloboda.portal.service.AddressService;
import balt.sloboda.portal.service.RequestsService;
import balt.sloboda.portal.service.ResidentService;
import balt.sloboda.portal.service.UserService;
import balt.sloboda.portal.service.exceptions.AlreadyExistsException;
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
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private RequestsService requestsService;

    @Autowired
    private ResidentService residentService;

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
    public ResponseEntity<?> register(@RequestBody NewUserRequestParams newUserRequestParams) {
        if (userService.alreadyExists(newUserRequestParams.getUserName())){
            return new ResponseEntity<>(new ErrorResponse("userAlreadyExists", new HashMap<String, String>() {{
                put("user", newUserRequestParams.getUserName());
            }}), HttpStatus.CONFLICT);
        }

        if (newUserRequestParams.getAddress() != null && newUserRequestParams.getAddress().getId() != null) {
            Optional<Address> addressById = addressService.getAddressById(newUserRequestParams.getAddress().getId());
            if (addressById.isPresent()){
                if (residentService.addressAlreadyUsed(addressById.get().getId())) { //address used by another user
                    return new ResponseEntity<>(new ErrorResponse("addressAlreadyUsed", null), HttpStatus.CONFLICT);
                }
                newUserRequestParams.setAddress(addressById.get());
            } else {
                // address not found
                return new ResponseEntity<>(new ErrorResponse("notExistingAddress", null), HttpStatus.CONFLICT);
            }
        } else {
            return new ResponseEntity<>(new ErrorResponse("notExistingAddress", null), HttpStatus.CONFLICT);
        }

        try {
            try {
                requestsService.createNewUserRequest(newUserRequestParams);
            } catch (AlreadyExistsException alreadyExistsException){
                return new ResponseEntity<>(new ErrorResponse(alreadyExistsException.getMessage(), new HashMap<String, String>() {{
                    put("user", newUserRequestParams.getUserName());
                }}), HttpStatus.CONFLICT);
            }
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
            return new ResponseEntity<>("tokenRefreshError", HttpStatus.UNAUTHORIZED);
        }

    }

    @RequestMapping(value = "/logout", method = RequestMethod.DELETE)
    public ResponseEntity<?> logout() throws Exception {
        tokenUtil.deleteTokens();
        return ResponseEntity.ok("");
    }

    @RequestMapping(value = "/reset-pass", method = RequestMethod.PUT)
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {

        if (resetPasswordRequest.getToken() == null || resetPasswordRequest.getToken().isEmpty()){
            return new ResponseEntity<>(new ErrorResponse("emptyPasswordResetToken", null), HttpStatus.NOT_FOUND);
        }

        Optional<User> byPasswordResetToken = userService.findByPasswordResetToken(resetPasswordRequest.getToken());
        if (byPasswordResetToken.isPresent()){
            if (resetPasswordRequest.getPassword().equals(resetPasswordRequest.getConfirmPassword())){
                // validate token
                if (!tokenUtil.validatePasswordResetToken(resetPasswordRequest.getToken(), byPasswordResetToken.get().getUserName())){
                    return new ResponseEntity<>(new ErrorResponse("tokenNotValidOrExpired", null), HttpStatus.CONFLICT);
                }
                userService.setNewPassword(byPasswordResetToken.get(), resetPasswordRequest.getPassword());
            } else {
                return new ResponseEntity<>(new ErrorResponse("passwordsNotMatch", null), HttpStatus.CONFLICT);
            }
        } else {
            return new ResponseEntity<>(new ErrorResponse("resetPasswordRequestNotFound", null), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok("");
    }


    @RequestMapping(value = "/request-pass", method = RequestMethod.POST)
    public ResponseEntity<?> requestPassword(@RequestBody RequestPasswordRequest requestPasswordRequest) {
        Optional<User> byUserName = userService.findByUserName(requestPasswordRequest.getEmail());
        if (byUserName.isPresent()){
            userService.requestPasswordReset(byUserName.get());
        } else {
            return new ResponseEntity<>(new ErrorResponse("userNotFound", new HashMap<String, String>() {{
                put("user", requestPasswordRequest.getEmail());
            }}), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok("");
    }
}
