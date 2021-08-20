package balt.sloboda.portal.configuration;

import balt.sloboda.portal.model.User;
import balt.sloboda.portal.model.request.RequestType;
import balt.sloboda.portal.model.request.predefined.NewUserRequest;
import balt.sloboda.portal.service.AddressService;
import balt.sloboda.portal.service.RequestsService;
import balt.sloboda.portal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

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

        if (!userService.alreadyExists(adminUser.getUserName())){
            userService.createUser(adminUser); //save user ToDo check if user cannot be saved because of id constraint
        }
        // create NewUserRequest RequestType
        RequestType requestType = new NewUserRequest().getRequestType();
        if (!requestsService.requestTypeAlreadyExists(requestType)){
            requestsService.saveRequestType(requestType);
        }
    }
}
