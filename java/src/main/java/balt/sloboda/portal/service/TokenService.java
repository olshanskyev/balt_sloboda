package balt.sloboda.portal.service;

import balt.sloboda.portal.model.JwtResponse;
import balt.sloboda.portal.model.RefreshTokenRequest;
import balt.sloboda.portal.model.TokenPair;
import balt.sloboda.portal.utils.JwtTokenUtil;
import balt.sloboda.portal.utils.WebSecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by evolshan on 11.07.2021.
 */
@Service
public class TokenService {

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private WebSecurityUtils webSecurityUtils;

    private Map<String, TokenPair> usedTokens = new HashMap<>();

    /**
     * generaes new tokens for user
     * @param authentication authentication information
     * @return JwtResponse with access_token and refresh_token
     */
    public JwtResponse generateTokens(Authentication authentication) {
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        final String token = jwtTokenUtil.generateAccessToken(userDetails);
        final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);
        TokenPair tokenPair = new TokenPair().accessToken(token).refreshToken(refreshToken);
        usedTokens.put(userDetails.getUsername(), tokenPair);
        return new JwtResponse(tokenPair);
    }

    /**
     * refreshes tokens after token-refresh opearion
     * @param refreshTokenRequest request with access and refresh token
     * @return new access token
     */
    public JwtResponse refreshTokens(RefreshTokenRequest refreshTokenRequest) throws TokenRefreshException {
        String usernameFromToken = jwtTokenUtil.getUsernameFromToken(refreshTokenRequest.getToken().getRefreshToken());
        UserDetails userDetails = userDetailsService.loadUserByUsername(usernameFromToken);

        // check refresh token expiration
        if (!jwtTokenUtil.validateToken(refreshTokenRequest.getToken().getRefreshToken(), userDetails)) {
            usedTokens.remove(usernameFromToken);
            throw new TokenRefreshException("Refresh token validation error");
        }

        TokenPair usedTokenPair = usedTokens.get(usernameFromToken);
        if (usedTokenPair != null &&  // check if access token and refresh token correlates with user
                usedTokenPair.getAccessToken().equals(refreshTokenRequest.getToken().getAccessToken()) &&
                usedTokenPair.getRefreshToken().equals(refreshTokenRequest.getToken().getRefreshToken())) {

            final String token = jwtTokenUtil.generateAccessToken(userDetails);
            TokenPair tokenPair = new TokenPair().accessToken(token).refreshToken(refreshTokenRequest.getToken().getRefreshToken());
            usedTokens.put(usernameFromToken, tokenPair);
            return (new JwtResponse(tokenPair));
        } else { //error
            usedTokens.remove(usernameFromToken);
            throw new TokenRefreshException("Cannot refresh Access Token. Stored tokens do not correlate with presented tokens");
        }

    }

    public void deleteTokens() {
        String authorizedUserName = webSecurityUtils.getAuthorizedUserName();
        if (authorizedUserName != null)
            usedTokens.remove(authorizedUserName);
    }


}
