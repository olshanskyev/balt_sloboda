package balt.sloboda.portal.utils;

/**
 * Created by evolshan on 11.07.2021.
 */
public class TokenRefreshException extends Exception {
    TokenRefreshException(String message) {
        super(message);
    }
    TokenRefreshException(String message, Throwable cause) {
        super(message, cause);
    }

}
