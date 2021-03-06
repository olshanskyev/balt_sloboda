package balt.sloboda.portal.model.request;

import balt.sloboda.portal.model.CalendarSelectionData;
import balt.sloboda.portal.model.Role;
import balt.sloboda.portal.model.User;
import balt.sloboda.portal.model.converter.RolesSetToStringConverter;
import balt.sloboda.portal.model.converter.GenericStringMapConverter;
import balt.sloboda.portal.model.converter.StringsMapToStringConverter;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name="REQUEST_TYPES")
public class RequestType {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="NAME", columnDefinition="varchar(64)", nullable = false, unique = true)
    private String name;

    @Column(name="TITLE", columnDefinition="varchar(256)", nullable = false)
    private String title;

    @Column(name= "DESCRIPTION", columnDefinition="varchar(512)", nullable = true)
    private String description;

    @Column(name="DURABLE", nullable = false)
    private boolean durable = false;

    @Column(name="ROLES", columnDefinition="varchar(256)", nullable = false)
    @Convert(converter = RolesSetToStringConverter.class)
    private Set<Role> roles = new HashSet<>(); // who can create such request

    @Column(name="SYSTEM_REQUEST", nullable = false)
    private boolean systemRequest = false;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "requestType")
    private List<RequestParam> parameters = new ArrayList<>();

    @OneToOne
    @JoinColumn(name="ASSIGN_TO_ID", nullable = false)
    private User assignTo;

    @Column(name="DISPLAY_OPTIONS", columnDefinition="varchar(512)", nullable = true)
    @Convert(converter = StringsMapToStringConverter.class)
    private Map<String, String> displayOptions; // in json {["parameterName":"parametervalue"], ...}

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="CALENDAR_SELECTION_ID", nullable = true)
    private CalendarSelectionData calendarSelection;

    @Column(name="REQUEST_ID_PREFIX", columnDefinition="varchar(3)", nullable = false)
    private String requestIdPrefix;

    public Long getId() {
        return id;
    }

    public RequestType setId(Long id) {
        this.id = id;
        return this;
    }

    public boolean isDurable() {
        return durable;
    }

    public RequestType setDurable(boolean durable) {
        this.durable = durable;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public RequestType setTitle(String title) {
        this.title = title;
        return this;
    }

    public List<RequestParam> getParameters() {
        return parameters;
    }

    public RequestType setParameters(List<RequestParam> parameters) {
        this.parameters = parameters;
        parameters.forEach(item -> item.setRequestType(this));
        return this;
    }

    public String getName() {
        return name;
    }

    public RequestType setName(String name) {
        this.name = name;
        return this;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public RequestType setRoles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public RequestType setDescription(String description) {
        this.description = description;
        return this;
    }

    public User getAssignTo() {
        return assignTo;
    }

    public RequestType setAssignTo(User assignTo) {
        this.assignTo = assignTo;
        return this;
    }

    public Map<String, String> getDisplayOptions() {
        return displayOptions;
    }

    public RequestType setDisplayOptions(Map<String, String> displayOptions) {
        this.displayOptions = displayOptions;
        return this;
    }

    public CalendarSelectionData getCalendarSelection() {
        return calendarSelection;
    }

    public RequestType setCalendarSelection(CalendarSelectionData calendarSelection) {
        this.calendarSelection = calendarSelection;
        return this;
    }

    public boolean isSystemRequest() {
        return systemRequest;
    }

    public RequestType setSystemRequest(boolean systemRequest) {
        this.systemRequest = systemRequest;
        return this;
    }

    public String getRequestIdPrefix() {
        return requestIdPrefix;
    }

    public RequestType setRequestIdPrefix(String requestIdPrefix) {
        this.requestIdPrefix = requestIdPrefix;
        return this;
    }
}

