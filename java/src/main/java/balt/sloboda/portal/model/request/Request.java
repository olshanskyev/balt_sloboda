package balt.sloboda.portal.model.request;

import balt.sloboda.portal.model.User;
import balt.sloboda.portal.model.converter.StringMapConverter;
import balt.sloboda.portal.model.converter.StringSetConverter;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name="REQUESTS")
public class Request {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="SUBJECT", columnDefinition="varchar(256)", nullable = false)
    private String subject;

    @Column(name="COMMENT", columnDefinition="varchar(512)", nullable = false)
    private String comment;

    @Column(name="PARAM_VALUES", columnDefinition="varchar(512)", nullable = false)
    @Convert(converter = StringMapConverter.class)
    private Map<String, String> paramValues; // in json {["parameterName":"parametervalue"], ...}

    @OneToOne
    @JoinColumn(name="REQUEST_TYPE_ID", nullable = false)
    private RequestType type;

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
    @JoinColumn(name="LAST_MODIFIED_BY_ID", nullable = false)
    private User lastModifiedBy;


    public Long getId() {
        return id;
    }

    public Request setId(Long id) {
        this.id = id;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public Request setSubject(String subject) {
        this.subject = subject;
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
}

