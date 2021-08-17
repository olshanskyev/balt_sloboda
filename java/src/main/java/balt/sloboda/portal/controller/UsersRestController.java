package balt.sloboda.portal.controller;

import balt.sloboda.portal.model.User;
import balt.sloboda.portal.service.DbUserService;
import balt.sloboda.portal.utils.WebSecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/")
public class UsersRestController {

    @Autowired
    private DbUserService dbUserService;

    @Autowired
    WebSecurityUtils webSecurityUtils;


    @RequestMapping(value="/userInfo", method = RequestMethod.GET)
    public ResponseEntity<?> getUserInfo() throws Exception {
        return getUserByName(webSecurityUtils.getAuthorizedUserName());

    }
    
    @RequestMapping(value="/management/users", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUsers() throws Exception {
        return new ResponseEntity<>(dbUserService.selectAll(), HttpStatus.OK);
    }

    @RequestMapping(value="/management/users/{userName}", method = RequestMethod.GET)
    public ResponseEntity<?> getUserByName(@PathVariable String userName) throws Exception {

        Optional<User> found = dbUserService.findByUserName(userName);
        if (!found.isPresent()) {
            return new ResponseEntity<>("User with name " + userName + " not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(found, HttpStatus.OK);

    }
}
