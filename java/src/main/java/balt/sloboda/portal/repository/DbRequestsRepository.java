package balt.sloboda.portal.repository;

import balt.sloboda.portal.model.request.Request;
import balt.sloboda.portal.model.request.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface DbRequestsRepository extends PagingAndSortingRepository<Request, Long> {
    Page<Request> findByTypeName(String requestTypeName, Pageable pageable);
    Page<Request> findByStatus(RequestStatus status, Pageable pageable);
    Page<Request> findByStatusAndTypeName(RequestStatus status, String requestTypeName, Pageable pageable);
    Page<Request> findByOwnerUserName(String userName, Pageable pageable);
    Page<Request> findByAssignedToUserName(String userName, Pageable pageable);
    Page<Request> findByOwnerUserNameAndStatusIn(String userName, List<RequestStatus> status, Pageable pageable);
    Page<Request> findByAssignedToUserNameAndStatusIn(String userName, List<RequestStatus> status, Pageable pageable);

}
