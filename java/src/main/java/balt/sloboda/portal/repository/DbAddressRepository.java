package balt.sloboda.portal.repository;

import balt.sloboda.portal.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DbAddressRepository extends JpaRepository<Address, Long> {
    @Query("SELECT DISTINCT a.street FROM Address a")
    List<String> findDistinctStreets();

    List<Address> findAllByStreet(String street);

    List<Address> findAddressById(Long id);

    List<Address> findAddressByStreetAndHouseNumberAndPlotNumber(String street, int houseNumber, int plotNumber);
}
