package balt.sloboda.portal.service;


import balt.sloboda.portal.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private DbUserService userService;

    @Override
    public UserDetails loadUserByUsername(String userName){

        User user = userService.findByUserName(userName);

        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                List<GrantedAuthority> list = new ArrayList<>();
                if (user != null) {
                    user.getRoles().forEach(item -> list.add((GrantedAuthority) item::toString));
                }
                return list;
            }

            @Override
            public String getPassword() {
                return (user != null)?user.getPassword(): null;
            }

            @Override
            public String getUsername() {
                return (user != null)?user.getUser(): null;
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };
    }
}
