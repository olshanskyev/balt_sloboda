package balt.sloboda.portal.configuration;

import balt.sloboda.portal.model.User;
import balt.sloboda.portal.model.request.RequestType;
import balt.sloboda.portal.model.request.predefined.NewUserRequestType;
import balt.sloboda.portal.service.AddressService;
import balt.sloboda.portal.service.RequestsService;
import balt.sloboda.portal.service.UserService;
import balt.sloboda.portal.service.exceptions.AlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Optional;

@Configuration
@Profile("!test")
public class DataLoader implements ApplicationRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private RequestsService requestsService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private User adminUser;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Optional<User> adminOptional = userService.findByUserName(adminUser.getUserName());
        //save user
        User admin = adminOptional.orElseGet(() -> userService.createUser(adminUser));
        // create NewUserRequest RequestType
        RequestType requestType = new NewUserRequestType().getRequestType();
        requestType.setAssignTo(admin);
        try {
            requestsService.saveRequestType(requestType);
        } catch (AlreadyExistsException ignored){
        }

    }
}
