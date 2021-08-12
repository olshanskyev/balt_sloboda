package balt.sloboda.portal.repository;

import balt.sloboda.portal.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DbAddressRepository extends JpaRepository<Address, Long> {
}
