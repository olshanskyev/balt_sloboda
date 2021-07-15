package balt.sloboda.portal.repository;

import balt.sloboda.portal.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Created by evolshan on 06.03.2020.
 */
@Repository
public interface DbAddressRepository extends JpaRepository<Address, Long> {
}
