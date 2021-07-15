package balt.sloboda.portal.controller;

import balt.sloboda.portal.model.JwtRequest;
import balt.sloboda.portal.model.JwtResponse;
import balt.sloboda.portal.model.RefreshTokenRequest;
import balt.sloboda.portal.service.JwtUserDetailsService;
import balt.sloboda.portal.service.TokenService;
import balt.sloboda.portal.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Created by evolshan on 07.07.2021.
 */
@RestController
@RequestMapping("/auth")
public class AuthRestController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @CrossOrigin
    public ResponseEntity<?> login(@RequestBody JwtRequest authenticationRequest) throws Exception {

        //authenticate via email and password using authenticationManager
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
            JwtResponse jwtResponse = tokenService.generateTokens(authentication);
            return ResponseEntity.ok(jwtResponse);
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }


    }

    @RequestMapping(value = "/refresh-token", method = RequestMethod.POST)
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) throws Exception {
        return ResponseEntity.ok(tokenService.refreshTokens(refreshTokenRequest));
    }

    @RequestMapping(value = "/logout", method = RequestMethod.DELETE)
    public ResponseEntity<?> logout() throws Exception {
        tokenService.deleteTokens();
        return ResponseEntity.ok("");
    }
}
