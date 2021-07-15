package balt.sloboda.portal.repository;

import balt.sloboda.portal.Application;
import balt.sloboda.portal.model.User;
import balt.sloboda.portal.service.DbAddressService;
import balt.sloboda.portal.service.DbUserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
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
public class DbUserServiceTest {

    @Autowired
    private DbUserService dbUserService;

    @Autowired
    private DbAddressService dbAddressService;

    @Test
    @Sql({"/create_users_data.sql"})
    @Sql(value = {"/remove_users_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllUsers()
            throws Exception {

        List<User> all = dbUserService.selectAll();
        Assert.assertEquals(2, all.size());
        User olshanskyev = all.stream().filter(item -> item.getUser().equals("olshanskyev@gmail.com")).findFirst().orElseThrow(RuntimeException::new);
        Assert.assertEquals(172, olshanskyev.getAddress().getPlotNumber());
        User admin = all.stream().filter(item -> item.getUser().equals("admin@baltsloboda2.ru")).findFirst().orElseThrow(RuntimeException::new);
        Assert.assertEquals(0, admin.getAddress().getPlotNumber());
    }
}
