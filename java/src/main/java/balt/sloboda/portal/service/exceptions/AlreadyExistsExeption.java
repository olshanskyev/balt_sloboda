package balt.sloboda.portal.service.exceptions;

public class AlreadyExistsExeption extends Exception {
    public AlreadyExistsExeption(String message) {
        super(message);
    }
    public AlreadyExistsExeption(String message, Throwable cause) {
        super(message, cause);
    }
}
