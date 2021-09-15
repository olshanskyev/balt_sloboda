package balt.sloboda.portal.service.exceptions;

public class UserNotAuthorizedException extends Exception {
    public UserNotAuthorizedException(String message) {
        super(message);
    }
    public UserNotAuthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
