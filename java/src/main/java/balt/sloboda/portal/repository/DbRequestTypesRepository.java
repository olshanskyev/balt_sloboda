package balt.sloboda.portal.repository;

import balt.sloboda.portal.model.request.RequestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DbRequestTypesRepository extends JpaRepository<RequestType, Long> {
    List<RequestType> findByName(String name);
}
