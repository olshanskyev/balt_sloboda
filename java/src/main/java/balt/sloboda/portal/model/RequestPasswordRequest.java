package balt.sloboda.portal.model;

import java.io.Serializable;

public class RequestPasswordRequest implements Serializable {

    private String email;


    public String getEmail() {
        return email;
    }

    public RequestPasswordRequest setEmail(String email) {
        this.email = email;
        return this;
    }
}