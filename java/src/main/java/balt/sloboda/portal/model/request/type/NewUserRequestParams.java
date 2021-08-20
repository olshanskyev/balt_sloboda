package balt.sloboda.portal.model.request.type;


import balt.sloboda.portal.model.Address;
import balt.sloboda.portal.model.request.RequestParam;
import balt.sloboda.portal.model.request.RequestParamType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewUserRequestParams implements RequestTypeParams {
    private String userName;
    private String firstName;
    private String lastName;
    private Address address;

    public String getUserName() {
        return userName;
    }

    public NewUserRequestParams setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public NewUserRequestParams setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public NewUserRequestParams setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Address getAddress() {
        return address;
    }

    public NewUserRequestParams setAddress(Address address) {
        this.address = address;
        return this;
    }

    public Map<String, String> buildValuesMap(){
        return new HashMap<String, String>(){{
            put("userName", getUserName());
            put("firstName", getFirstName());
            put("lastName", getLastName());
            put("street", getAddress().getStreet());
            put("houseNumber", String.valueOf(address.getHouseNumber()));
            put("plotNumber", String.valueOf(address.getPlotNumber()));
        }};
    }
    public static List<RequestParam> getParamsDefinition() {
        return Arrays.asList(
                new RequestParam().setName("userName").setOptional(false).setType(RequestParamType.STRING).setComment("userName"),
                new RequestParam().setName("firstName").setOptional(false).setType(RequestParamType.STRING).setComment("firstName"),
                new RequestParam().setName("lastName").setOptional(false).setType(RequestParamType.STRING).setComment("lastName"),
                new RequestParam().setName("street").setOptional(false).setType(RequestParamType.STRING).setComment("street"),
                new RequestParam().setName("houseNumber").setOptional(false).setType(RequestParamType.INTEGER).setComment("houseNumber"),
                new RequestParam().setName("plotNumber").setOptional(false).setType(RequestParamType.INTEGER).setComment("plotNumber")
        );
    }
}
