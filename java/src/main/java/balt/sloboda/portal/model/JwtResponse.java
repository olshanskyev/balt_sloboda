package balt.sloboda.portal.model;

import java.io.Serializable;

public class JwtResponse implements Serializable {
    private static final long serialVersionUID = -8091879091924046844L;

    private TokenPair token;

    public JwtResponse(TokenPair tokenPair) {
        this.token = tokenPair;
    }
    public JwtResponse() {}
    public void setToken(TokenPair token) {
        this.token = token;
    }
    public TokenPair getToken() {
        return this.token;
    }

}