package balt.sloboda.portal.utils;

import balt.sloboda.portal.model.Role;
import balt.sloboda.portal.model.request.RequestStatus;
import balt.sloboda.portal.service.exceptions.RequestLifecycleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class RequestLifecycleUtil {

    @Autowired
    private WebSecurityUtils webSecurityUtils;

    // user can only reject NEW AND ACCEPTED requests
    // admin can set different states
    public void checkStatusChanging(RequestStatus currentState, RequestStatus newState) throws RequestLifecycleException {
        if (webSecurityUtils.isAdmin())
            switch (currentState) {
                case NEW:
                case ACCEPTED:
                case IN_PROGRESS:
                case CLOSED:
                case REJECTED:
            }
        else {
            switch (currentState) {
                case NEW:
                case ACCEPTED: {
                    if (!Arrays.asList(RequestStatus.REJECTED).contains(newState))
                        throw new RequestLifecycleException("userCannotChangeStatus", currentState, newState); ; // NEW => REJECTED
                    return;
                }
                case IN_PROGRESS:
                case CLOSED:
                case REJECTED: {
                    throw new RequestLifecycleException("userCannotChangeStatus", currentState, newState);
                }
            }
        }

    }
}
