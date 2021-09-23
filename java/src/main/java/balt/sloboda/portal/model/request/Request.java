package balt.sloboda.portal.model.request;

import balt.sloboda.portal.model.CalendarSelectionData;
import balt.sloboda.portal.model.User;
import balt.sloboda.portal.model.converter.GenericStringMapConverter;
import balt.sloboda.portal.model.converter.StringsMapToStringConverter;
import balt.sloboda.portal.model.converter.StringsSetToStringConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name="REQUESTS")
public class Request {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="COMMENT", columnDefinition="varchar(512)", nullable = true)
    private String comment;

    @Column(name="GENERATED_IDENTIFIER", columnDefinition="varchar(10)", nullable = false)
    private String generatedIdentifier;

    @Column(name="PARAM_VALUES", columnDefinition="varchar(512)", nullable = true)
    @Convert(converter = StringsMapToStringConverter.class)
    private Map<String, String> paramValues; // in json {["parameterName":"parametervalue"], ...}

    @OneToOne
    @JoinColumn(name="REQUEST_TYPE_ID", nullable = false)
    private RequestType type;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="CALENDAR_SELECTION_ID", nullable = true)
    private CalendarSelectionData calendarSelection;

    @Column(name="CREATION_DATE")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @CreationTimestamp
    private LocalDateTime createDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name="STATUS", nullable = false)
    private RequestStatus status = RequestStatus.NEW;

    @Column(name="LAST_MODIFIED_DATE")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @UpdateTimestamp
    private LocalDateTime lastModifiedDate;

    @OneToOne
    @JoinColumn(name="OWNER_ID", nullable = false)
    private User owner;

    @OneToOne
    @JoinColumn(name="ASSIGNED_TO_ID", nullable = false)
    private User assignedTo;

    @OneToOne
    @JoinColumn(name="LAST_MODIFIED_BY_ID", nullable = true)
    private User lastModifiedBy;


    public Long getId() {
        return id;
    }

    public Request setId(Long id) {
        this.id = id;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public Request setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public User getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Request setLastModifiedBy(User lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }


    public RequestType getType() {
        return type;
    }

    public Request setType(RequestType requestType) {
        this.type = requestType;
        return this;
    }

    public User getOwner() {
        return owner;
    }

    public Request setOwner(User owner) {
        this.owner = owner;
        return this;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public Request setStatus(RequestStatus status) {
        this.status = status;
        return this;
    }


    public Map<String, String> getParamValues() {
        return paramValues;
    }

    public Request setParamValues(Map<String, String> paramValues) {
        this.paramValues = paramValues;
        return this;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public Request setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
        return this;
    }

    public CalendarSelectionData getCalendarSelection() {
        return calendarSelection;
    }

    public Request setCalendarSelection(CalendarSelectionData calendarSelection) {
        this.calendarSelection = calendarSelection;
        return this;
    }

    @PrePersist
    void prePersist() {
        if (this.generatedIdentifier == null || this.generatedIdentifier.isEmpty())
            this.generatedIdentifier = this.type.getRequestIdPrefix();
    }

    public String getGeneratedIdentifier() {
        return generatedIdentifier;
    }

    public Request setGeneratedIdentifier(String generatedIdentifier) {
        this.generatedIdentifier = generatedIdentifier;
        return this;
    }
}

