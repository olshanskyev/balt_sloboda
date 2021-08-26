package balt.sloboda.portal.controller;

import balt.sloboda.portal.Application;
import balt.sloboda.portal.model.*;
import balt.sloboda.portal.model.request.type.NewUserRequestParams;
import balt.sloboda.portal.service.UserService;
import balt.sloboda.portal.service.EmailService;
import balt.sloboda.portal.utils.JsonUtils;
import balt.sloboda.portal.utils.JwtTokenUtil;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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

import org.mockito.ArgumentMatchers;


import static org.hamcrest.Matchers.*;
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
    private UserService userService;

    @Value("${jwt.accessTokenValidity}")
    private long ACCESS_TOKEN_VALIDITY;
    @Value("${jwt.refreshTokenValidity}")
    private long REFRESH_TOKEN_VALIDITY;
    @Value("${jwt.passwordResetTokenValidity}")
    private long PASSWORD_RESET_TOKEN_VALIDITY;

    public static JwtResponse login(String userName, String password, MockMvc mvc) throws Exception {
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

    public void logOut(String accessToken) throws Exception {
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

    public static JwtResponse adminLogin(MockMvc mvc) throws Exception {
        return login("admin@baltsloboda2.ru", "pwd#bs2", mvc);
    }

    public static JwtResponse userLogin(MockMvc mvc) throws Exception {
        return login("olshanskyev@gmail.com", "Jkmifycrbq", mvc);
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
        JwtResponse jwtResponse = adminLogin(mvc);
        // 2. get users ok
        getUsers_OK(jwtResponse.getToken().getAccessToken());

        // 1. user login
        jwtResponse = userLogin(mvc);
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
        JwtResponse jwtResponse = adminLogin(mvc);
        // 2. get users ok
        getUsers_OK(jwtResponse.getToken().getAccessToken());
        // 3. logout, token deleted
        logOut(jwtResponse.getToken().getAccessToken());
        // 4. operation not authorized
        getUsers_401(jwtResponse.getToken().getAccessToken());


        // 1. admin login
        jwtResponse = adminLogin(mvc);
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
        JwtResponse jwtResponse1 = adminLogin(mvc);
        // 2. second admin login
        JwtResponse jwtResponse2 = adminLogin(mvc);
        // 3. get with second login is ok
        getUsers_OK(jwtResponse2.getToken().getAccessToken());
        // 4. get with first login fails
        getUsers_401(jwtResponse1.getToken().getAccessToken());
    }


    @Autowired
    private EmailService emailService;

    public static void registerUserOK(MockMvc mvc, NewUserRequestParams okUser) throws Exception {
        mvc.perform(post("/auth/register")
                .content(JsonUtils.asJsonString(okUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Sql({"/create_users_data.sql", "/create_request_types_data.sql"})
    @Sql(value = {"/remove_request_types_data.sql", "/remove_users_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void registerTest() throws Exception {

        NewUserRequestParams alreadyExistsUser = new NewUserRequestParams().setUserName("olshanskyev@gmail.com").setFirstName("Evgeny").setLastName("Olshansky").setAddress(new Address().setId(102L));
        NewUserRequestParams notExistingAddressUser = new NewUserRequestParams().setUserName("test@gmail.com").setFirstName("Evgeny").setLastName("Olshansky").setAddress(new Address().setId(111L));
        NewUserRequestParams addressAlreadyUsedUser = new NewUserRequestParams().setUserName("test@gmail.com").setFirstName("Evgeny").setLastName("Olshansky").setAddress(new Address().setId(2L));
        NewUserRequestParams nullAddressIdUser = new NewUserRequestParams().setUserName("olshanskyevdev@gmail.com").setFirstName("Evgeny").setLastName("Olshansky").setAddress(new Address().setId(null));
        NewUserRequestParams okUser = new NewUserRequestParams().setUserName("olshanskyevdev@gmail.com").setFirstName("Evgeny").setLastName("Olshansky").setAddress(new Address().setId(4L));

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

        Mockito.doNothing().when(emailService).sendUserRegistrationRequestConfirmation(okUser.getUserName());
        registerUserOK(mvc, okUser);

        mvc.perform(post("/auth/register")
                .content(JsonUtils.asJsonString(okUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is("newUserRequestAlreadyExists")));
    }


    public static void resetPassOK(MockMvc mvc, ResetPasswordRequest resetPasswordRequest) throws Exception {
        mvc.perform(put("/auth/reset-pass")
                .content(JsonUtils.asJsonString(resetPasswordRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Test
    @Sql({"/create_users_data.sql", "/create_request_types_data.sql"})
    @Sql(value = {"/remove_request_types_data.sql", "/remove_users_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void resetPassNegativeTest() throws Exception{
        //String token = jwtTokenUtil.generatePasswordResetToken("olshanskyevdev@gmail.com");
        ResetPasswordRequest resetEmptyToken = new ResetPasswordRequest().setPassword("test").setConfirmPassword("test").setToken(null);
        ResetPasswordRequest resetTokenNotFound = new ResetPasswordRequest().setPassword("test").setConfirmPassword("test").setToken("asdf");
        ResetPasswordRequest resetPassNotMatch = new ResetPasswordRequest().setPassword("test").setConfirmPassword("test2").setToken("PASSWORD_RESET_TOKEN");
        ResetPasswordRequest resetNotValidToken = new ResetPasswordRequest().setPassword("test").setConfirmPassword("test").setToken("PASSWORD_RESET_TOKEN");

        mvc.perform(put("/auth/reset-pass")
                .content(JsonUtils.asJsonString(resetEmptyToken))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("emptyPasswordResetToken")));

        mvc.perform(put("/auth/reset-pass")
                .content(JsonUtils.asJsonString(resetTokenNotFound))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("resetPasswordRequestNotFound")));

        mvc.perform(put("/auth/reset-pass")
                .content(JsonUtils.asJsonString(resetPassNotMatch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error", is("passwordsNotMatch")));

        mvc.perform(put("/auth/reset-pass")
                .content(JsonUtils.asJsonString(resetNotValidToken))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error", is("tokenNotValidOrExpired")));

    }


    public static void requestPassOK(MockMvc mvc, RequestPasswordRequest requestPasswordRequest) throws Exception {
        mvc.perform(post("/auth/request-pass")
                .content(JsonUtils.asJsonString(requestPasswordRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    @Sql({"/create_users_data.sql", "/create_request_types_data.sql"})
    @Sql(value = {"/remove_request_types_data.sql", "/remove_users_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void requestAndResetPassTest() throws Exception{

        RequestPasswordRequest requestNotFound = new RequestPasswordRequest().setEmail("olshanskyev2@gmail.com");
        RequestPasswordRequest requestOk = new RequestPasswordRequest().setEmail("olshanskyev@gmail.com");

        // 1. request password with not existing user (error)
        mvc.perform(post("/auth/request-pass")
                .content(JsonUtils.asJsonString(requestNotFound))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("userNotFound")));

        final String[] args = new String[2];
        Mockito.doAnswer(i -> {
            args[0] = (String)i.getArguments()[0];
            args[1] = (String)i.getArguments()[1];
            return null;
        }).when(emailService).sendPasswordResetLink(ArgumentMatchers.eq("olshanskyev@gmail.com"), ArgumentMatchers.anyString());
        // 2. request password and get token (args[1] from mocking method)
        requestPassOK(mvc, requestOk);
        // 3. reset password
        resetPassOK(mvc, new ResetPasswordRequest().setPassword("1234").setConfirmPassword("1234").setToken(args[1]));

        // 4. login with new password
        login("olshanskyev@gmail.com", "1234", mvc);

        // 5. check what user has null reset token
        Assert.assertNull(userService.findByUserName("olshanskyev@gmail.com").get().getPasswordResetToken());

    }

    @Test
    @Sql({"/create_users_data.sql", "/create_request_types_data.sql"})
    @Sql(value = {"/remove_request_types_data.sql", "/remove_users_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void requestTokenExpirationTest() throws Exception{

        RequestPasswordRequest requestOk = new RequestPasswordRequest().setEmail("olshanskyev@gmail.com");

        final String[] args = new String[2];
        Mockito.doAnswer(i -> {
            args[0] = (String)i.getArguments()[0];
            args[1] = (String)i.getArguments()[1];
            return null;
        }).when(emailService).sendPasswordResetLink(ArgumentMatchers.eq("olshanskyev@gmail.com"), ArgumentMatchers.anyString());
        // 1. request password and get token (args[1] from mocking method)
        requestPassOK(mvc, requestOk);

        // 2. wait until token expired
        Thread.sleep(PASSWORD_RESET_TOKEN_VALIDITY * 1000);

        // 3. reset password
        ResetPasswordRequest resetExpiredToken = new ResetPasswordRequest().setPassword("test").setConfirmPassword("test").setToken(args[1]);
        mvc.perform(put("/auth/reset-pass")
                .content(JsonUtils.asJsonString(resetExpiredToken))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error", is("tokenNotValidOrExpired")));

        // 4. login fails
        mvc.perform(post("/auth/login")
                .content(JsonUtils.asJsonString(new JwtRequest("olshanskyev@gmail.com", "1234")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

}
