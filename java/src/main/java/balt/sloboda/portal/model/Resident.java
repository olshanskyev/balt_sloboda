package balt.sloboda.portal.model;

import javax.persistence.*;

@Entity
@Table(name="RESIDENTS")
public class Resident {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="FIRST_NAME", columnDefinition="varchar(256)", nullable = false)
    private String firstName;

    @Column(name="LAST_NAME", columnDefinition="varchar(256)", nullable = false)
    private String lastName;

    @OneToOne
    @JoinColumn(name="ADDRESS_ID", nullable = false)
    private Address address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="USER_ID", nullable = false)
    private User user;

    public Long getId() {
        return id;
    }

    public Resident setId(Long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Resident setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Resident setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Address getAddress() {
        return address;
    }

    public Resident setAddress(Address address) {
        this.address = address;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Resident setUser(User user) {
        this.user = user;
        return this;
    }
}
