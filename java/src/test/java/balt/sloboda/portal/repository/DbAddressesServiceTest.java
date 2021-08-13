package balt.sloboda.portal.repository;

import balt.sloboda.portal.Application;
import balt.sloboda.portal.model.Address;
import balt.sloboda.portal.service.DbAddressService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(properties = {
        "spring.config.location = classpath:application_test.yml",
})
@ActiveProfiles("test")
public class DbAddressesServiceTest {

    @Autowired
    private DbAddressService dbAddressService;

    @Test
    @Sql({"/create_users_data.sql"})
    @Sql(value = {"/remove_users_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAddressesTest() {
        List<String> constAllStreets = Arrays.asList("Solnechnaya", "Pokrovskaya", "Svetlaya");
        List<Address> constSolnechnayaAddresses = Arrays.asList(
                new Address().street("Solnechnaya").houseNumber(34).plotNumber(172),
                new Address().street("Solnechnaya").houseNumber(32).plotNumber(171));

        List<Address> addresses = dbAddressService.selectAll();
        Assert.assertEquals(5, addresses.size());
        List<String> gotAllStreets = dbAddressService.getAllStreets();
        Assert.assertEquals(3, gotAllStreets.size());
        Assert.assertTrue(gotAllStreets.containsAll(constAllStreets) && constAllStreets.containsAll(gotAllStreets));
        List<Address> gotAddressesOnSolnechnaya = dbAddressService.getAddressesOnStreet("Solnechnaya");
        Assert.assertEquals(2, gotAddressesOnSolnechnaya.size());
        Assert.assertTrue(gotAddressesOnSolnechnaya.containsAll(constSolnechnayaAddresses) &&
                constSolnechnayaAddresses.containsAll(gotAddressesOnSolnechnaya));

    }
}
