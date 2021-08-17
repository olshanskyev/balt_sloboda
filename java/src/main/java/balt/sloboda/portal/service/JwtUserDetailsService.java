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
import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private DbUserService userService;

    @Override
    public UserDetails loadUserByUsername(String userName){

        Optional<User> user = userService.findByUserName(userName);

        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                List<GrantedAuthority> list = new ArrayList<>();
                user.ifPresent(value -> value.getRoles().forEach(item -> list.add((GrantedAuthority) item::toString)));
                return list;
            }

            @Override
            public String getPassword() {
                return user.map(User::getPassword).orElse(null);
            }

            @Override
            public String getUsername() {
                return user.map(User::getUser).orElse(null);
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
