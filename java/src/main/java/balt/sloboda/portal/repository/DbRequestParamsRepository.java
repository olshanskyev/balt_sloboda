package balt.sloboda.portal.repository;

import balt.sloboda.portal.model.request.RequestParam;
import balt.sloboda.portal.model.request.RequestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DbRequestParamsRepository extends JpaRepository<RequestParam, Long> {
    List<RequestParam> findByRequestTypeName(String requestTypeName);
}
