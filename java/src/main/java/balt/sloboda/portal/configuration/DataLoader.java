package balt.sloboda.portal.configuration;

import balt.sloboda.portal.model.request.RequestType;
import balt.sloboda.portal.model.request.predefined.NewUserRequest;
import balt.sloboda.portal.service.DbRequestsService;
import balt.sloboda.portal.service.DbUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader implements ApplicationRunner {

    @Autowired
    private DbUserService dbUserService;

    @Autowired
    private DbRequestsService dbRequestsService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // create NewUserRequest RequestType
        /*RequestType requestType = NewUserRequest.getRequestType();
        if (!dbRequestsService.requestTypeAlreadyExists(requestType)){
            dbRequestsService.saveRequestType(requestType);
        }*/
    }
}
