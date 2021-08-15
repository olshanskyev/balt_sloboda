package balt.sloboda.portal.service;

import balt.sloboda.portal.Application;
import balt.sloboda.portal.model.Role;
import balt.sloboda.portal.model.request.RequestType;
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
public class DbRequestsServiceTest {

    @Autowired
    private DbRequestsService dbRequestsService;

    @Test
    @Sql({"/create_request_types_data.sql"})
    @Sql(value = {"/remove_request_types_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllRequestsTest() {
        Assert.assertEquals(2, dbRequestsService.getAllRequestTypes().size());
        List<RequestType> availableForUser = dbRequestsService.getRequestTypesAvailableForUser();
        Assert.assertEquals(1, availableForUser.size());
        availableForUser.
                forEach(item -> Assert.assertTrue(item.getRoles().contains(Role.ROLE_USER)));

        // ToDo get Request by type and check params

    }
}
