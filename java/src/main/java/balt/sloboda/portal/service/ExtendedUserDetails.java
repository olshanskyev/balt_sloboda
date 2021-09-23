package balt.sloboda.portal.service;

import balt.sloboda.portal.model.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface ExtendedUserDetails extends UserDetails {
    User getUser();
}
