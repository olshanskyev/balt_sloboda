package balt.sloboda.portal.configuration;

import balt.sloboda.portal.model.Address;
import balt.sloboda.portal.model.User;
import balt.sloboda.portal.model.request.RequestType;
import balt.sloboda.portal.model.request.predefined.NewUserRequest;
import balt.sloboda.portal.service.DbAddressService;
import balt.sloboda.portal.service.DbRequestsService;
import balt.sloboda.portal.service.DbUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Optional;

@Configuration
@Profile("!test")
public class DataLoader implements ApplicationRunner {

    @Autowired
    private DbUserService dbUserService;

    @Autowired
    private DbRequestsService dbRequestsService;

    @Autowired
    private DbAddressService dbAddressService;

    @Autowired
    private User adminUser;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (!dbUserService.alreadyExists(adminUser)){
            dbUserService.createUser(adminUser); //save user
        }
        // create NewUserRequest RequestType
        RequestType requestType = new NewUserRequest().getRequestType();
        if (!dbRequestsService.requestTypeAlreadyExists(requestType)){
            dbRequestsService.saveRequestType(requestType);
        }
    }
}
