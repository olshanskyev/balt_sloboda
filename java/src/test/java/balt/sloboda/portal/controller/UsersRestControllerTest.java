package balt.sloboda.portal.controller;

import balt.sloboda.portal.Application;
import balt.sloboda.portal.model.JwtRequest;
import balt.sloboda.portal.model.JwtResponse;
import balt.sloboda.portal.model.User;
import balt.sloboda.portal.service.DbUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by evolshan on 13.07.2021.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = { "spring.config.location = classpath:application_test.yml",

})
@ActiveProfiles("test")
public class UsersRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private DbUserService dbUserService;


    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T fromJsonString(String jsonString, Class<T> valueType) {
        try {
            return (new ObjectMapper()).readValue(jsonString, valueType);
        } catch (JsonProcessingException e) {
            return null;
        }
    }


    private JwtResponse login(String userName, String password) throws Exception {
        JwtRequest jwtRequest = new JwtRequest(userName, password);
        MvcResult result = mvc.perform(post("/auth/login")
                .content(asJsonString(jwtRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        JwtResponse jwtResponse = fromJsonString(result.getResponse().getContentAsString(), JwtResponse.class);
        Assert.assertNotNull(jwtResponse);
        Assert.assertNotNull(jwtResponse.getToken());
        Assert.assertNotEquals(jwtResponse.getToken().getRefreshToken(), jwtResponse.getToken().getAccessToken());
        return jwtResponse;
    }


    private JwtResponse adminLogin() throws Exception {
        return login("admin@baltsloboda2.ru", "pwd#bs2");
    }

    private JwtResponse userLogin() throws Exception {
        return login("olshanskyev@gmail.com", "Jkmifycrbq");
    }

    @Test
    @Sql({"/create_users_data.sql"})
    @Sql(value = {"/remove_users_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getUsers() throws Exception {
        JwtResponse jwtResponse = adminLogin();
        mvc.perform(get("/management/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtResponse.getToken().getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));


        jwtResponse = userLogin();
        mvc.perform(get("/management/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtResponse.getToken().getAccessToken()))
                .andExpect(status().isForbidden());
    }

}
