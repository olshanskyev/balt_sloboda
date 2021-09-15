package balt.sloboda.portal.service.exceptions;

import balt.sloboda.portal.model.request.RequestStatus;

public class RequestLifecycleException extends Exception {
    private RequestStatus from;
    private RequestStatus to;

    public RequestLifecycleException(String message, RequestStatus from, RequestStatus to) {
        super(message);
        this.from = from;
        this.to = to;
    }
    public RequestLifecycleException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestStatus getFrom() {
        return from;
    }

    public RequestStatus getTo() {
        return to;
    }


}
