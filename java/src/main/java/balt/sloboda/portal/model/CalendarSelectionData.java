package balt.sloboda.portal.model;

import balt.sloboda.portal.model.converter.BooleansMapToStringConverter;
import balt.sloboda.portal.model.converter.EveryDaysMapToStringConverter;
import balt.sloboda.portal.model.converter.StringsSetToStringConverter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name="CALENDAR_SELECTION")
public class CalendarSelectionData {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name="SELECTION_MODE", columnDefinition="varchar(64)", nullable = false)
    private SelectionMode selectionMode;

    @Column(name="MONTH_DAYS", columnDefinition="varchar(512)", nullable = true)
    @Convert(converter = EveryDaysMapToStringConverter.class)
    private Map<String, List<Integer>> monthDays;

    @Column(name="WEEK_DAYS", columnDefinition="varchar(256)", nullable = true)
    @Convert(converter = BooleansMapToStringConverter.class)
    private Map<String, Boolean> weekDays;

    @Column(name="SELECTED_DAYS", columnDefinition="varchar(512)", nullable = true)
    @Convert(converter = StringsSetToStringConverter.class)
    private Set<String> selectedDays;

    public Long getId() {
        return id;
    }

    public CalendarSelectionData setId(Long id) {
        this.id = id;
        return this;
    }

    public SelectionMode getSelectionMode() {
        return selectionMode;
    }

    public CalendarSelectionData setSelectionMode(SelectionMode selectionMode) {
        this.selectionMode = selectionMode;
        return this;
    }

    public Map<String, List<Integer>> getMonthDays() {
        return monthDays;
    }

    public CalendarSelectionData setMonthDays(Map<String, List<Integer>> monthDays) {
        this.monthDays = monthDays;
        return this;
    }

    public Map<String, Boolean> getWeekDays() {
        return weekDays;
    }

    public CalendarSelectionData setWeekDays(Map<String, Boolean> weekDays) {
        this.weekDays = weekDays;
        return this;
    }

    public Set<String> getSelectedDays() {
        return selectedDays;
    }

    public CalendarSelectionData setSelectedDays(Set<String> selectedDays) {
        this.selectedDays = selectedDays;
        return this;
    }
}
