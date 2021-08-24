package balt.sloboda.portal.utils;

import balt.sloboda.portal.service.JwtUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class WebSecurityUtils {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(WebSecurityUtils.class);

    /**
     *
     * @param request
     * @return Authentication if success or null if something went wrong
     */
    public Authentication authenticateRequest(HttpServletRequest request){

        String username = null;
        String jwtToken = null;
        final String requestTokenHeader = request.getHeader("Authorization");

// JWT Token is in the form "Bearer token". Remove Bearer word and get
// only the Token
            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                logger.error("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                logger.error("JWT Token has expired");
            }
        } else {
            logger.info("JWT Token does not begin with Bearer String");
        }

// Once we get the token validate it.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

// if token is valid configure authentication that should be set in Spring
            if (jwtTokenUtil.validateAcessToken(jwtToken, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                return usernamePasswordAuthenticationToken;
            }
        }
        return null;
    }


    /**
     *
     * @return authorized user name
     */
    public String getAuthorizedUserName() {
        Object authentication = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (authentication instanceof UserDetails)
            return ((UserDetails)authentication).getUsername();
        else
            return null;
    }

}
