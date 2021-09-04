package balt.sloboda.portal.controller;

import balt.sloboda.portal.Application;
import balt.sloboda.portal.model.*;
import balt.sloboda.portal.model.request.*;
import balt.sloboda.portal.model.request.predefined.NewUserRequestType;
import balt.sloboda.portal.model.request.type.NewUserRequestParams;
import balt.sloboda.portal.service.EmailService;
import balt.sloboda.portal.service.RequestsService;
import balt.sloboda.portal.service.UserService;
import balt.sloboda.portal.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.hamcrest.collection.IsMapContaining;
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
import org.springframework.test.web.servlet.MvcResult;

import java.util.*;

import static org.hamcrest.Matchers.*;
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

    @Test
    @Sql({"/create_users_data.sql", "/create_request_types_data.sql"})
    @Sql(value = {"/remove_request_types_data.sql", "/remove_users_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createRequestTypeTest() throws Exception {
        // 1. Login with admin
        JwtResponse jwtResponse = AuthRestControllerTest.adminLogin(mvc);
        RequestType requestTypeWeekly = new RequestType()
                .setTitle("Вывоз мусора")
                .setAssignTo(new User().setId(1L))
                .setDurable(true)
                .setCalendarSelection(
                        new CalendarSelectionData().setSelectionMode(SelectionMode.Weekly).setWeekDays(
                                new HashMap<String, Boolean>(){{
                                    put("Monday", false);
                                    put("Tuesday", true);
                                }}
                        ))
                .setParameters(new ArrayList<>(Arrays.asList(
                        new RequestParam().setName("Объем бака").setOptional(false).setDefaultValue("240").setType(RequestParamType.INTEGER)
                        )))
                .setDisplayOptions(
                        new HashMap<String, String>(){{
                            put("icon", "nb-icon-name");
                            put("showInMainRequestMenu", "true");
                        }}
                );

        // 2.1. create new weekly request type
        mvc.perform(post("/management/requestTypes")
                .content(JsonUtils.asJsonString(requestTypeWeekly))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtResponse.getToken().getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Vyvozmusora")))
                .andExpect(jsonPath("$.calendarSelection.weekDays", hasEntry("Monday", false)));

        // 2.2 check new type created
        mvc.perform(get("/management/requestTypes")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtResponse.getToken().getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        // as string because of problem while parsing LocalDate as json
        String requestDays = "{\"durable\":true,\"title\":\"Request Заявка\",\"description\":\"\",\"parameters\":[{\"type\":\"ENUM\",\"optional\":false,\"name\":\"Объем бака\",\"defaultValue\":\"240\",\"enumValues\":[\"120\",\"240\"]}],\"calendarSelection\":{\"selectedDays\":[\"2021-09-13T21:00:00.000Z\",\"2021-09-17T21:00:00.000Z\"],\"selectionMode\":\"Manually\"},\"displayOptions\":{\"icon\":\"bell\",\"showInMainRequestMenu\":true},\"assignTo\":{\"id\":2}}";


        // 3.1. create new weekly request type
        mvc.perform(post("/management/requestTypes")
                .content(requestDays)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtResponse.getToken().getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("RequestZaavka")))
                .andExpect(jsonPath("$.calendarSelection.selectedDays", hasSize(2)));

        // 3.2 check new type created
        mvc.perform(get("/management/requestTypes")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtResponse.getToken().getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[3].calendarSelection.selectedDays", hasSize(2)))
                .andExpect(jsonPath("$[3].calendarSelection.selectedDays", containsInAnyOrder("2021-09-13", "2021-09-17")))
                .andExpect(jsonPath("$[3].parameters", hasSize(1)))
                .andExpect(jsonPath("$[3].parameters[0].type", is("ENUM")))
                .andExpect(jsonPath("$[3].parameters[0].enumValues",  hasSize(2)))
                .andExpect(jsonPath("$[3].parameters[0].enumValues",  containsInAnyOrder("120", "240")))
                .andExpect(jsonPath("$[3].displayOptions",  hasEntry("icon", "bell")))
                .andExpect(jsonPath("$[3].displayOptions",  hasEntry("showInMainRequestMenu", "true")));


    }




    @Test
    @Sql({"/create_users_data.sql", "/create_request_types_data.sql"})
    @Sql(value = {"/remove_request_types_data.sql", "/remove_users_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getUserRequestTypeTest() throws Exception {
        // 1. Login with user
        JwtResponse jwtResponse = AuthRestControllerTest.userLogin(mvc);
        // 2 check request types
        mvc.perform(get("/requestTypes")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtResponse.getToken().getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("GarbageRemovalRequest")))
                .andExpect(jsonPath("$[0].roles", hasItem("ROLE_USER")));

        // 3. Login with admin
        jwtResponse = AuthRestControllerTest.adminLogin(mvc);
        // 4 check request types
        mvc.perform(get("/requestTypes")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtResponse.getToken().getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }


}
