package balt.sloboda.portal.controller;

import balt.sloboda.portal.Application;
import balt.sloboda.portal.model.*;
import balt.sloboda.portal.service.DbUserService;
import balt.sloboda.portal.utils.JsonUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = { "spring.config.location = classpath:application_test.yml",
})
@ActiveProfiles("test")
public class AuthRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private DbUserService dbUserService;

    @Value("${jwt.accessTokenValidity}")
    private long ACCESS_TOKEN_VALIDITY;
    @Value("${jwt.refreshTokenValidity}")
    private long REFRESH_TOKEN_VALIDITY;




    private JwtResponse login(String userName, String password) throws Exception {
        JwtRequest jwtRequest = new JwtRequest(userName, password);
        MvcResult result = mvc.perform(post("/auth/login")
                .content(JsonUtils.asJsonString(jwtRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        JwtResponse jwtResponse = JsonUtils.fromJsonString(result.getResponse().getContentAsString(), JwtResponse.class);
        Assert.assertNotNull(jwtResponse);
        Assert.assertNotNull(jwtResponse.getToken());
        Assert.assertNotEquals(jwtResponse.getToken().getRefreshToken(), jwtResponse.getToken().getAccessToken());
        return jwtResponse;
    }

    private void logOut(String accessToken) throws Exception {
        mvc.perform(delete("/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

    }

    private JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest) throws Exception {
        MvcResult mvcResult = mvc.perform(post("/auth/refresh-token")
                .content(JsonUtils.asJsonString(refreshTokenRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        JwtResponse jwtResponse = JsonUtils.fromJsonString(mvcResult.getResponse().getContentAsString(), JwtResponse.class);
        Assert.assertNotNull(jwtResponse);
        Assert.assertNotNull(jwtResponse.getToken());
        return jwtResponse;
    }


    private void refreshToken_NOK(RefreshTokenRequest refreshTokenRequest) throws Exception {
        mvc.perform(post("/auth/refresh-token")
                .content(JsonUtils.asJsonString(refreshTokenRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(401));

    }

    private JwtResponse adminLogin() throws Exception {
        return login("admin@baltsloboda2.ru", "pwd#bs2");
    }

    private JwtResponse userLogin() throws Exception {
        return login("olshanskyev@gmail.com", "Jkmifycrbq");
    }


    private void getUsers_OK(String accessToken) throws Exception {
        mvc.perform(get("/management/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    private void getUsers_401(String accessToken) throws Exception {
        mvc.perform(get("/management/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().is(401));
    }

    @Test
    @Sql({"/create_users_data.sql"})
    @Sql(value = {"/remove_users_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getUsersTest() throws Exception {
        // 1. admin login
        JwtResponse jwtResponse = adminLogin();
        // 2. get users ok
        getUsers_OK(jwtResponse.getToken().getAccessToken());

        // 1. user login
        jwtResponse = userLogin();
        // 2. get users not ok
        mvc.perform(get("/management/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtResponse.getToken().getAccessToken()))
                .andExpect(status().isForbidden());
    }


    @Test
    @Sql({"/create_users_data.sql"})
    @Sql(value = {"/remove_users_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void authTest() throws Exception {
        // 1. admin login
        JwtResponse jwtResponse = adminLogin();
        // 2. get users ok
        getUsers_OK(jwtResponse.getToken().getAccessToken());
        // 3. logout, token deleted
        logOut(jwtResponse.getToken().getAccessToken());
        // 4. operation not authorized
        getUsers_401(jwtResponse.getToken().getAccessToken());


        // 1. admin login
        jwtResponse = adminLogin();
        getUsers_OK(jwtResponse.getToken().getAccessToken());
        // 2. token expires
        Thread.sleep(ACCESS_TOKEN_VALIDITY * 1000);
        // 3. operation not authorized
        getUsers_401(jwtResponse.getToken().getAccessToken());

        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setToken(jwtResponse.getToken());
        // 4. refresh admin token
        jwtResponse = refreshToken(refreshTokenRequest);
        // 5. get users ok
        getUsers_OK(jwtResponse.getToken().getAccessToken());
        // 6. refresh token expires
        Thread.sleep(REFRESH_TOKEN_VALIDITY * 1000);
        refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setToken(jwtResponse.getToken());
        // 6. trying to refresh with expired refresh token not ok
        mvc.perform(post("/auth/refresh-token")
                .content(JsonUtils.asJsonString(refreshTokenRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(401));
    }



    @Test
    @Sql({"/create_users_data.sql"})
    @Sql(value = {"/remove_users_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void multipleAuthTest() throws Exception {
        // 1. first admin login
        JwtResponse jwtResponse1 = adminLogin();
        // 2. second admin login
        JwtResponse jwtResponse2 = adminLogin();
        // 3. get with second login is ok
        getUsers_OK(jwtResponse2.getToken().getAccessToken());
        // 4. get with first login fails
        getUsers_401(jwtResponse1.getToken().getAccessToken());
    }


    @Test
    @Sql({"/create_users_data.sql", "/create_request_types_data.sql"})
    @Sql(value = {"/remove_request_types_data.sql", "/remove_users_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void registerTest() throws Exception {
        User alreadyExistsUser = new User().setUser("olshanskyev@gmail.com").setFirstName("Evgeny").setLastName("Olshansky").setAddress(new Address().setId(102L));
        User notExistingAddressUser = new User().setUser("test@gmail.com").setFirstName("Evgeny").setLastName("Olshansky").setAddress(new Address().setId(111L));
        User addressAlreadyUsedUser = new User().setUser("test@gmail.com").setFirstName("Evgeny").setLastName("Olshansky").setAddress(new Address().setId(2L));
        User nullAddressIdUser = new User().setUser("olshanskyevdev@gmail.com").setFirstName("Evgeny").setLastName("Olshansky").setAddress(new Address().setId(null));
        User okUser = new User().setUser("olshanskyevdev@gmail.com").setFirstName("Evgeny").setLastName("Olshansky").setAddress(new Address().setId(4L));

        mvc.perform(post("/auth/register")
                .content(JsonUtils.asJsonString(alreadyExistsUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is("userAlreadyExists")));

        mvc.perform(post("/auth/register")
                .content(JsonUtils.asJsonString(notExistingAddressUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is("notExistingAddress")));

        mvc.perform(post("/auth/register")
                .content(JsonUtils.asJsonString(addressAlreadyUsedUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is("addressAlreadyUsed")));

        mvc.perform(post("/auth/register")
                .content(JsonUtils.asJsonString(nullAddressIdUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is("notExistingAddress")));

        mvc.perform(post("/auth/register")
                .content(JsonUtils.asJsonString(okUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }



}
