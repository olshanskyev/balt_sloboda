package balt.sloboda.portal.repository;

import balt.sloboda.portal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DbUserRepository extends JpaRepository<User, Long> {
    List<User> findByUser(String user);
    List<User> findByAddressId(Long addressId);
}
