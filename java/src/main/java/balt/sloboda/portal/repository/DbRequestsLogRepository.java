package balt.sloboda.portal.repository;

import balt.sloboda.portal.model.request.RequestLogItem;
import balt.sloboda.portal.model.request.RequestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DbRequestsLogRepository extends JpaRepository<RequestLogItem, Long> {
    List<RequestLogItem> findByRequestId(Long requestId);
}
