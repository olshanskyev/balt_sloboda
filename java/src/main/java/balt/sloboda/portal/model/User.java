package balt.sloboda.portal.model;

import balt.sloboda.portal.model.converter.StringSetConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="USERS")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="USER_NAME", columnDefinition="varchar(256)", nullable = false)
    private String userName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name="PASSWORD", columnDefinition="varchar(256)", nullable = false)
    private String password;

    @Column(name="ROLES", columnDefinition="varchar(256)", nullable = false)
    @Convert(converter = StringSetConverter.class)
    private Set<Role> roles = new HashSet<>();

    
    public Long getId() {
        return id;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public User setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public User setRoles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }


}
