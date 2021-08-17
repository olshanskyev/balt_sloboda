package balt.sloboda.portal.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="ADDRESSES")
public class Address {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="STREET", columnDefinition="varchar(256)", nullable = false)
    private String street;

    @Column(name="HOUSE_NUMBER", nullable = false)
    private int houseNumber;

    @Column(name="PLOT_NUMBER")
    private int plotNumber;

    public String getStreet() {
        return street;
    }

    public Address setStreet(String street) {
        this.street = street;
        return this;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public Address setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
        return this;
    }

    public int getPlotNumber() {
        return plotNumber;
    }

    public Address setPlotNumber(int plotNumber) {
        this.plotNumber = plotNumber;
        return this;
    }

    public Long getId() {
        return id;
    }

    public Address setId(Long id) {
        this.id = id;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        // self check
        if (this == o)
            return true;
        // null check
        if (o == null)
            return false;
        // type check and cast
        if (getClass() != o.getClass())
            return false;
        Address address = (Address) o;
        // field comparison
        return Objects.equals(street, address.street)
                && Objects.equals(houseNumber, address.houseNumber)
                && Objects.equals(plotNumber, address.plotNumber);
    }
}
