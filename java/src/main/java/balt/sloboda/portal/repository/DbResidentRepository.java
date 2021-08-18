package balt.sloboda.portal.repository;

import balt.sloboda.portal.model.Resident;
import balt.sloboda.portal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DbResidentRepository extends JpaRepository<Resident, Long> {
    List<Resident> findByAddressId(Long addressId);
    List<Resident> findByUserUserName(String userName);
}
