package balt.sloboda.portal.service;

import balt.sloboda.portal.Application;
import balt.sloboda.portal.model.Address;
import org.junit.Assert;
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
    private AddressService addressService;

    @Test
    @Sql({"/create_users_data.sql"})
    @Sql(value = {"/remove_users_data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAddressesTest() {
        List<String> constAllStreets = Arrays.asList("Solnechnaya", "Pokrovskaya", "Svetlaya");
        List<Address> constSolnechnayaAddresses = Arrays.asList(
                new Address().setStreet("Solnechnaya").setHouseNumber(34).setPlotNumber(172),
                new Address().setStreet("Solnechnaya").setHouseNumber(32).setPlotNumber(171));

        List<Address> addresses = addressService.selectAll();
        Assert.assertEquals(5, addresses.size());
        List<String> gotAllStreets = addressService.getAllStreets();
        Assert.assertEquals(3, gotAllStreets.size());
        Assert.assertTrue(gotAllStreets.containsAll(constAllStreets) && constAllStreets.containsAll(gotAllStreets));
        List<Address> gotAddressesOnSolnechnaya = addressService.getAddressesOnStreet("Solnechnaya");
        Assert.assertEquals(2, gotAddressesOnSolnechnaya.size());
        Assert.assertTrue(gotAddressesOnSolnechnaya.containsAll(constSolnechnayaAddresses) &&
                constSolnechnayaAddresses.containsAll(gotAddressesOnSolnechnaya));

    }


}
