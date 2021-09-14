package balt.sloboda.portal.repository;

import balt.sloboda.portal.model.request.Request;
import balt.sloboda.portal.model.request.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DbRequestsRepository extends JpaRepository<Request, Long> {
    List<Request> findByTypeName(String requestTypeName);
    List<Request> findByStatus(RequestStatus status);
    List<Request> findByStatusAndTypeName(RequestStatus status, String requestTypeName);
    List<Request> findByOwnerUserName(String userName);
    List<Request> findByOwnerUserNameAndStatusIn(String userName, List<RequestStatus> status);

}
