package balt.sloboda.portal.controller;

import balt.sloboda.portal.model.ErrorResponse;
import balt.sloboda.portal.model.User;
import balt.sloboda.portal.service.UserService;
import balt.sloboda.portal.utils.WebSecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class UsersRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private WebSecurityUtils webSecurityUtils;

    @RequestMapping(value="/management/users", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUsers() throws Exception {
        return new ResponseEntity<>(userService.selectAllUsers(), HttpStatus.OK);
    }

    @RequestMapping(value="/userInfo", method = RequestMethod.GET)
    public ResponseEntity<?> getUserInfo() throws Exception {
        String userName = webSecurityUtils.getAuthorizedUserName();
        return getUserByName(userName);
    }

    @RequestMapping(value="/management/users/{userName}", method = RequestMethod.GET)
    public ResponseEntity<?> getUserByName(@PathVariable String userName){

        Optional<User> found = userService.findByUserName(userName);
        if (!found.isPresent()) {
            return new ResponseEntity<>(new ErrorResponse("userInfoNotFound", new HashMap<String, String>() {{
                put("user", userName);
            }}), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(found, HttpStatus.OK);

    }
}
