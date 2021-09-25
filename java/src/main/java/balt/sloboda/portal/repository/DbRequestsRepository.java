package balt.sloboda.portal.repository;

import balt.sloboda.portal.model.request.Request;
import balt.sloboda.portal.model.request.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface DbRequestsRepository extends PagingAndSortingRepository<Request, Long> {
    Page<Request> findByTypeName(String requestTypeName, Pageable pageable);
    Page<Request> findByStatus(RequestStatus status, Pageable pageable);
    Page<Request> findByStatusAndTypeName(RequestStatus status, String requestTypeName, Pageable pageable);
    Page<Request> findByOwnerUserName(String userName, Pageable pageable);
    Page<Request> findByOwnerUserNameAndTypeName(String userName, String typeName, Pageable pageable);
    Page<Request> findByAssignedToUserName(String userName, Pageable pageable);
    Page<Request> findByAssignedToUserNameAndTypeName(String userName, String typeName, Pageable pageable);
    Page<Request> findByOwnerUserNameAndStatusIn(String userName, List<RequestStatus> status, Pageable pageable);
    Page<Request> findByOwnerUserNameAndTypeNameAndStatusIn(String userName, String typeName, List<RequestStatus> status, Pageable pageable);
    Page<Request> findByAssignedToUserNameAndStatusIn(String userName, List<RequestStatus> status, Pageable pageable);
    Page<Request> findByAssignedToUserNameAndTypeNameAndStatusIn(String userName, String typeName, List<RequestStatus> status, Pageable pageable);

    @Query("SELECT r.type.name as requestTypeName, count(r.type.name) as count FROM Request r group by(r.type.name)")
    List<IRequestsCount> findAllRequestsCount();

    @Query("SELECT r.type.name as requestTypeName, count(r.type.name) as count FROM Request r WHERE r.owner.userName = ?1 group by(r.type.name)")
    List<IRequestsCount> findRequestsCountByOwner(String username);

    @Query("SELECT r.type.name as requestTypeName, count(r.type.name) as count FROM Request r WHERE r.owner.userName = ?1 AND r.status IN ?2 group by(r.type.name)")
    List<IRequestsCount> findRequestsCountByOwnerAndStatus(String username, List<RequestStatus> status);

    @Query("SELECT r.type.name as requestTypeName, count(r.type.name) as count FROM Request r WHERE r.assignedTo.userName = ?1 group by(r.type.name)")
    List<IRequestsCount> findRequestsCountByAssignedTo(String username);

    @Query("SELECT r.type.name as requestTypeName, count(r.type.name) as count FROM Request r WHERE r.assignedTo.userName = ?1 AND r.status IN ?2 group by(r.type.name)")
    List<IRequestsCount> findRequestsCountByAssignedToAndStatus(String username, List<RequestStatus> status);

}
