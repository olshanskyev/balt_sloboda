package balt.sloboda.portal.model.request.type;


import balt.sloboda.portal.model.Address;

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
}
