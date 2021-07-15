package balt.sloboda.portal.model;

/**
 * Created by evolshan on 11.07.2021.
 */
public class RefreshTokenRequest {
    private TokenPair token;

    public TokenPair getToken() {
        return this.token;
    }

    public void setToken(TokenPair token){
        this.token = token;
    }
}
