package balt.sloboda.portal.controller;

import balt.sloboda.portal.Application;
import balt.sloboda.portal.model.Address;
import balt.sloboda.portal.model.JwtResponse;
import balt.sloboda.portal.model.ResetPasswordRequest;
import balt.sloboda.portal.model.User;
import balt.sloboda.portal.model.request.Request;
import balt.sloboda.portal.model.request.RequestStatus;
import balt.sloboda.portal.model.request.predefined.NewUserRequestType;
import balt.sloboda.portal.model.request.type.NewUserRequestParams;
import balt.sloboda.portal.service.EmailService;
import balt.sloboda.portal.service.RequestsService;
import balt.sloboda.portal.service.UserService;
import balt.sloboda.portal.utils.JsonUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = { "spring.config.location = classpath:application_test.yml",
})
@ActiveProfiles("test")
public class RequestRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RequestsService requestsService;

    @Autowired
    private UserService userService;

    @Test
    @Sql({"/create_users_data.sql", "/create_request_types_data.sql"})
    @Sql(value = {"/remove_request_types_data.sql", "/remove_users_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createUserTest() throws Exception {

        NewUserRequestParams okUser = new NewUserRequestParams().setUserName("olshanskyevdev@gmail.com").setFirstName("Evgeny").setLastName("Olshansky").setAddress(new Address().setId(4L));

        Mockito.doNothing().when(emailService).sendUserRegistrationRequestConfirmation(okUser.getUserName());
        // 1. register new user
        AuthRestControllerTest.registerUserOK(mvc, okUser);
        // 2. login with admin
        JwtResponse jwtResponse = AuthRestControllerTest.adminLogin(mvc);
        // 3. check request exists
        List<Request> allRequestByStatusAndType = requestsService.getAllRequestByStatusAndType(RequestStatus.NEW, new NewUserRequestType().getName());
        Optional<Request> newUserRequest = allRequestByStatusAndType.stream().filter(item -> item.getParamValues().get("userName").equals("olshanskyevdev@gmail.com")).findFirst();
        Assert.assertTrue(newUserRequest.isPresent());
        // 4. accept new user request
        mvc.perform(put("/management/requests/" + newUserRequest.get().getId() + "/accept")
                .header("Authorization", "Bearer " + jwtResponse.getToken().getAccessToken()))
                .andExpect(status().isOk());
        // 5. check user created
        mvc.perform(get("/management/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtResponse.getToken().getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)));


        allRequestByStatusAndType = requestsService.getAllRequestByStatusAndType(RequestStatus.CLOSED, new NewUserRequestType().getName());
        newUserRequest = allRequestByStatusAndType.stream().filter(item -> item.getParamValues().get("userName").equals("olshanskyevdev@gmail.com")).findFirst();
        Assert.assertTrue(newUserRequest.isPresent());
        User createdUser = userService.findByUserName("olshanskyevdev@gmail.com").get();
        Assert.assertNotNull(createdUser.getPasswordResetToken());

        // 6. set new password
        AuthRestControllerTest.resetPassOK(mvc, new ResetPasswordRequest().setPassword("test").setConfirmPassword("test").setToken(createdUser.getPasswordResetToken()));
        // 7. login with new user
        AuthRestControllerTest.login("olshanskyevdev@gmail.com", "test", mvc);


    }



}
