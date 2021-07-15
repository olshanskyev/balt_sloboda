package balt.sloboda.portal.repository;

import balt.sloboda.portal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by evolshan on 06.03.2020.
 */
@Repository
public interface DbUserRepository extends JpaRepository<User, Long> {
    List<User> findByUser(String user);
}
