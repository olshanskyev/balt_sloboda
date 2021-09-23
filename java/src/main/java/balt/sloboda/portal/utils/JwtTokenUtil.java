package balt.sloboda.portal.utils;


import balt.sloboda.portal.model.JwtResponse;
import balt.sloboda.portal.model.RefreshTokenRequest;
import balt.sloboda.portal.model.TokenPair;
import balt.sloboda.portal.model.User;
import balt.sloboda.portal.service.JwtUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

    @Value("${jwt.accessTokenValidity}")
    private long ACCESS_TOKEN_VALIDITY;
    @Value("${jwt.refreshTokenValidity}")
    private long REFRESH_TOKEN_VALIDITY;

    @Value("${jwt.passwordResetTokenValidity}")
    private long PASSWORD_RESET_TOKEN_VALIDITY;

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    private WebSecurityUtils webSecurityUtils;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    //retrieve username from jwt token
    String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    //generate access token for user
    private String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        return doGenerateJwtToken(claims, userDetails.getUsername(), ACCESS_TOKEN_VALIDITY * 1000);
    }

    private String generateRefreshToken(UserDetails userDetails) {
        return doGenerateJwtToken(new HashMap<>(), userDetails.getUsername(), REFRESH_TOKEN_VALIDITY * 1000);
    }



    //while creating the token -
//1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
//2. Sign the JWT using the HS512 algorithm and secret key.
//3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
//   compaction of the JWT to a URL-safe string

    //UsersConfig.User user;
    private String doGenerateJwtToken(Map<String, Object> claims, String subject, long expirationMs) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    //validate token
    Boolean validateAcessToken(String token, UserDetails userDetails) {
        Optional<Map.Entry<String, TokenPair>> foundToken = usedTokens.entrySet().stream().filter
                (item -> item.getValue().getAccessToken().equals(token)).findFirst();
        if (!foundToken.isPresent()) {
            logger.warn("Access token not found in used tokens. Can be deleted via logout or new login");
            return false; // token previously not used or deleted via logout
        }
        try {
            final String username = getUsernameFromToken(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (ExpiredJwtException exc) {
            // token expired
            // remove entry from used tokens
            usedTokens.remove(foundToken.get().getKey());
            logger.warn("Access token expired");
            return false;
        }
    }

    private Map<String, TokenPair> usedTokens = new HashMap<>();

    /**
     * generaes new tokens for user
     * @param authentication authentication information
     * @return JwtResponse with access_token and refresh_token
     */
    public JwtResponse generateTokens(Authentication authentication) {
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        final String token = generateAccessToken(userDetails);
        final String refreshToken = generateRefreshToken(userDetails);
        TokenPair tokenPair = new TokenPair().setAccessToken(token).setRefreshToken(refreshToken);
        usedTokens.put(userDetails.getUsername(), tokenPair);
        return new JwtResponse(tokenPair);
    }


    public void deleteTokens() {
        User authorizedUser = webSecurityUtils.getAuthorizedUser();
        if (authorizedUser != null && authorizedUser.getUserName() != null)
            usedTokens.remove(authorizedUser.getUserName());
    }

    /**
     * refreshes tokens after token-refresh opearion
     * @param refreshTokenRequest request with access and refresh token
     * @return new access token
     */
    public JwtResponse refreshTokens(RefreshTokenRequest refreshTokenRequest) throws TokenRefreshException {
        String usernameFromToken;
        String token = refreshTokenRequest.getToken().getRefreshToken();
        // start validating refresh token
        // 1. if not found in used tokens throw exception
        Optional<Map.Entry<String, TokenPair>> foundToken = usedTokens.entrySet().stream().filter
                (item -> item.getValue().getRefreshToken().equals(token)).findFirst();
        if (!foundToken.isPresent()) {
            logger.warn("Refresh token is not in used list");
            throw new TokenRefreshException("Refresh token is not in used list"); // token previously not used or deleted via logout
        }
        try {
            // this throws ExpiredJwtException so we can't get username
            usernameFromToken = getUsernameFromToken(refreshTokenRequest.getToken().getRefreshToken());
        } catch (ExpiredJwtException exc) {
            // 2. token expired
            // remove entry from used tokens
            usedTokens.remove(foundToken.get().getKey());
            logger.warn("Refresh token expired");
            throw new TokenRefreshException("Refresh token expired");
        }

        TokenPair usedTokenPair = usedTokens.get(usernameFromToken);
        if (usedTokenPair != null &&  // check if access token correlates with user
                usedTokenPair.getAccessToken().equals(refreshTokenRequest.getToken().getAccessToken())) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(usernameFromToken);
            final String newToken = generateAccessToken(userDetails);
            TokenPair tokenPair = new TokenPair().setAccessToken(newToken).setRefreshToken(refreshTokenRequest.getToken().getRefreshToken());
            usedTokens.put(usernameFromToken, tokenPair);
            return (new JwtResponse(tokenPair));
        } else { // // 3. access token in used set is not equal to token from refresh request
            usedTokens.remove(usernameFromToken);
            logger.warn("Cannot refresh Access Token. Stored access token do not correlate with in request presented token");
            throw new TokenRefreshException("Cannot refresh Access Token. Stored access token do not correlate with in request presented token");
        }

    }

    public String generatePasswordResetToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateJwtToken(claims, userName, PASSWORD_RESET_TOKEN_VALIDITY * 1000);
    }

    public Boolean validatePasswordResetToken(String token, String userName) {
        try {
            return getUsernameFromToken(token).equals(userName);
        }  catch (ExpiredJwtException exc) {
            return false;
        } catch (Exception ex) {
            return false;
        }
    }


}
