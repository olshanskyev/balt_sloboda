package balt.sloboda.portal.model.request;

import balt.sloboda.portal.model.Role;
import balt.sloboda.portal.model.converter.StringSetConverter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="REQUEST_TYPES")
public class RequestType {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="NAME", columnDefinition="varchar(64)", nullable = false, unique = true)
    private String name;

    @Column(name="TITLE", columnDefinition="varchar(256)", nullable = false)
    private String title;

    @Column(name="DURABLE", nullable = false)
    private boolean durable = false;

    @Column(name="ROLES", columnDefinition="varchar(256)", nullable = false)
    @Convert(converter = StringSetConverter.class)
    private Set<Role> roles = new HashSet<>(); // who can create such request

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "REQUEST_TYPE_ID")
    private List<RequestParam> parameters = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public RequestType id(Long id) {
        this.id = id;
        return this;
    }

    public boolean isDurable() {
        return durable;
    }

    public RequestType durable(boolean durable) {
        this.durable = durable;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public RequestType title(String title) {
        this.title = title;
        return this;
    }

    public List<RequestParam> getParameters() {
        return parameters;
    }

    public RequestType parameters(List<RequestParam> parameters) {
        this.parameters = parameters;
        return this;
    }

    public String getName() {
        return name;
    }

    public RequestType name(String name) {
        this.name = name;
        return this;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public RequestType roles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }
}

