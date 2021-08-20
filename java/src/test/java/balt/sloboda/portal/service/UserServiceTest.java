package balt.sloboda.portal.service;

import balt.sloboda.portal.Application;
import balt.sloboda.portal.model.Resident;
import balt.sloboda.portal.model.Role;
import balt.sloboda.portal.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(properties = {
        "spring.config.location = classpath:application_test.yml",
})
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ResidentService residentService;

    @Test
    @Sql({"/create_users_data.sql"})
    @Sql(value = {"/remove_users_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllUsers() {

        List<User> all = userService.selectAllUsers();
        Assert.assertEquals(2, all.size());
        User olshanskyev = all.stream().filter(item -> item.getUserName().equals("olshanskyev@gmail.com")).findFirst().orElseThrow(RuntimeException::new);
        Assert.assertTrue(olshanskyev.getRoles().contains(Role.ROLE_USER) && !olshanskyev.getRoles().contains(Role.ROLE_ADMIN));
        User admin = all.stream().filter(item -> item.getUserName().equals("admin@baltsloboda2.ru")).findFirst().orElseThrow(RuntimeException::new);
        Assert.assertTrue(admin.getRoles().contains(Role.ROLE_ADMIN));
    }

    //ToDo add addresses and resident tests

    @Test
    @Sql({"/create_users_data.sql"})
    @Sql(value = {"/remove_users_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addressAlreadyUsed() {
        List<Resident> all = residentService.selectAllResidents();
        Assert.assertTrue(residentService.addressAlreadyUsed(all.get(0).getAddress().getId()));
        Assert.assertFalse(residentService.addressAlreadyUsed(1111L));
    }

}
