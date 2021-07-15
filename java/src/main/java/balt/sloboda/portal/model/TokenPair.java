package balt.sloboda.portal.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by evolshan on 11.07.2021.
 */
public class TokenPair {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;


    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public TokenPair accessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public TokenPair refreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }
}
