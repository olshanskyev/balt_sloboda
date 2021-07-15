package balt.sloboda.portal.model;

import javax.persistence.*;

/**
 * Created by evolshan on 09.07.2021.
 */
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

    public Address street(String street) {
        this.street = street;
        return this;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public Address houseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
        return this;
    }

    public int getPlotNumber() {
        return plotNumber;
    }

    public Address plotNumber(int plotNumber) {
        this.plotNumber = plotNumber;
        return this;
    }

    public Long getId() {
        return id;
    }

    public Address id(Long id) {
        this.id = id;
        return this;
    }
}
