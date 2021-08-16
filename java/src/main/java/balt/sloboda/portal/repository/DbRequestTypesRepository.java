package balt.sloboda.portal.repository;

import balt.sloboda.portal.model.Role;
import balt.sloboda.portal.model.request.RequestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface DbRequestTypesRepository extends JpaRepository<RequestType, Long> {
    List<RequestType> findByName(String name);
}
