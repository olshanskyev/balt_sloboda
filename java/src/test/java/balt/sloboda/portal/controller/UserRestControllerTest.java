package balt.sloboda.portal.controller;

import balt.sloboda.portal.Application;
import balt.sloboda.portal.model.Address;
import balt.sloboda.portal.model.JwtResponse;
import balt.sloboda.portal.model.request.type.NewUserRequestParams;
import balt.sloboda.portal.service.EmailService;
import balt.sloboda.portal.utils.JsonUtils;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = { "spring.config.location = classpath:application_test.yml",
})
@ActiveProfiles("test")
public class UserRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private EmailService emailService;

    @Test
    @Sql({"/create_users_data.sql", "/create_request_types_data.sql"})
    @Sql(value = {"/remove_request_types_data.sql", "/remove_users_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void acceptUser() throws Exception {

        NewUserRequestParams okUser = new NewUserRequestParams().setUserName("olshanskyevdev@gmail.com").setFirstName("Evgeny").setLastName("Olshansky").setAddress(new Address().setId(4L));

        Mockito.doNothing().when(emailService).sendUserRegistrationRequestConfirmation(okUser.getUserName());
        // send register request
        mvc.perform(post("/auth/register")
                .content(JsonUtils.asJsonString(okUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        JwtResponse jwtResponse = AuthRestControllerTest.adminLogin(mvc);


        mvc.perform(put("/management/users/olshanskyevdev@gmail.com/accept")
                .header("Authorization", "Bearer " + jwtResponse.getToken().getAccessToken()))
                .andExpect(status().isOk());

        mvc.perform(get("/management/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtResponse.getToken().getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)));
        // ToDo check status

    }



}
