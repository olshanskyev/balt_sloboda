package balt.sloboda.portal.repository;

import balt.sloboda.portal.model.request.Request;
import balt.sloboda.portal.model.request.RequestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DbRequestsRepository extends JpaRepository<Request, Long> {
    List<Request> findByTypeName(String requestTypeName);
    List<Request> findByOwnerUser(String userName);
}
