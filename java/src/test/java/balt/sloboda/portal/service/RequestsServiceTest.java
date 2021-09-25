package balt.sloboda.portal.service;

import balt.sloboda.portal.Application;
import balt.sloboda.portal.model.request.Request;
import balt.sloboda.portal.model.request.RequestParam;
import balt.sloboda.portal.model.request.RequestType;

import balt.sloboda.portal.repository.IRequestsCount;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
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
public class RequestsServiceTest {


    @Autowired
    private RequestsService requestsService;

    @Test
    @Sql({"/create_users_data.sql", "/create_request_types_data.sql"})
    @Sql(value = {"/remove_request_types_data.sql", "/remove_users_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllRequestsTest() throws Exception{
        List<RequestType> allRequestTypes = requestsService.getAllRequestTypes();
        Assert.assertEquals(3, allRequestTypes.size());

        List<RequestParam> paramsByRequestType = requestsService.getParamsByRequestType("NewUserRequest");
        Assert.assertEquals(6, paramsByRequestType.size());

        Page<Request> newUserRequest = requestsService.getAllRequestByType("NewUserRequest", 0, 10);
        Assert.assertEquals(1, newUserRequest.getContent().size());

        List<IRequestsCount> requestsCountGroupByType = requestsService.getAllCurrentUserRequestsCount();
        Assert.assertEquals(3, requestsCountGroupByType.size());
        requestsCountGroupByType.forEach(item -> {
            if (item.getRequestTypeName().equals("GarbageRemovalRequest")){
                Assert.assertEquals(new Long(4), item.getCount());
            }
            if (item.getRequestTypeName().equals("NewUserRequest")){
                Assert.assertEquals(new Long(1), item.getCount());
            }
            if (item.getRequestTypeName().equals("GarbageRemovalRequest2")){
                Assert.assertEquals(new Long(1), item.getCount());
            }
        });
    }
}
