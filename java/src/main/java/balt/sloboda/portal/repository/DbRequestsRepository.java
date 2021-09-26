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

    Page<Request> findByOwnerUserNameAndTypeSystemRequest(String userName, boolean systemRequest, Pageable pageable);
    Page<Request> findByOwnerUserNameAndTypeNameAndTypeSystemRequest(String userName, String typeName, boolean systemRequest, Pageable pageable);
    Page<Request> findByAssignedToUserNameAndTypeSystemRequest(String userName, boolean systemRequest, Pageable pageable);
    Page<Request> findByAssignedToUserNameAndTypeNameAndTypeSystemRequest(String userName, String typeName, boolean systemRequest, Pageable pageable);
    Page<Request> findByOwnerUserNameAndStatusInAndTypeSystemRequest(String userName, List<RequestStatus> status, boolean systemRequest, Pageable pageable);
    Page<Request> findByOwnerUserNameAndTypeNameAndStatusInAndTypeSystemRequest(String userName, String typeName, List<RequestStatus> status, boolean systemRequest, Pageable pageable);
    Page<Request> findByAssignedToUserNameAndStatusInAndTypeSystemRequest(String userName, List<RequestStatus> status, boolean systemRequest, Pageable pageable);
    Page<Request> findByAssignedToUserNameAndTypeNameAndStatusInAndTypeSystemRequest(String userName, String typeName, List<RequestStatus> status, boolean systemRequest, Pageable pageable);

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
