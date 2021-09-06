package balt.sloboda.portal.repository;

import balt.sloboda.portal.model.CalendarSelectionData;
import balt.sloboda.portal.model.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DbCalendarSelectionRepository extends JpaRepository<CalendarSelectionData, Long> {
}
