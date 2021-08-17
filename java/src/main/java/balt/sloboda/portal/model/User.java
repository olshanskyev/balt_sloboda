package balt.sloboda.portal.model;

import balt.sloboda.portal.model.converter.StringSetConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cascade;

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
    private String user;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name="PASSWORD", columnDefinition="varchar(256)", nullable = false)
    private String password;

    @Column(name="ROLES", columnDefinition="varchar(256)", nullable = false)
    @Convert(converter = StringSetConverter.class)
    private Set<Role> roles = new HashSet<>();

    @Column(name="FIRST_NAME", columnDefinition="varchar(256)", nullable = false)
    private String firstName;

    @Column(name="LAST_NAME", columnDefinition="varchar(256)", nullable = false)
    private String lastName;

    @OneToOne
    @JoinColumn(name="ADDRESS_ID", nullable = false)
    private Address address;
    
    public Long getId() {
        return id;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUser() {
        return user;
    }

    public User setUser(String user) {
        this.user = user;
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

    public String getFirstName() {
        return firstName;
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Address getAddress() {
        return address;
    }

    public User setAddress(Address address) {
        this.address = address;
        return this;
    }
}
