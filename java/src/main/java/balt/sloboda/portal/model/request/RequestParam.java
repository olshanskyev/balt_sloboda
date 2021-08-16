package balt.sloboda.portal.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name="REQUEST_PARAMS")
public class RequestParam {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="REQUEST_TYPE_ID", nullable = false)
    @JsonIgnore
    private RequestType requestType;

    @Column(name="NAME", columnDefinition="varchar(64)", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name="TYPE", columnDefinition="varchar(64)", nullable = false)
    private RequestParamType type;

    @Column(name="OPTIONAL", nullable = false)
    private boolean optional = false;

    @Column(name="COMMENT", columnDefinition="varchar(128)", nullable = true)
    private String comment;

    public Long getId() {
        return id;
    }

    public RequestParam id(Long id) {
        this.id = id;
        return this;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public RequestParam requestType(RequestType requestType) {
        this.requestType = requestType;
        return this;
    }

    public String getName() {
        return name;
    }

    public RequestParam name(String name) {
        this.name = name;
        return this;
    }

    public RequestParamType getType() {
        return type;
    }

    public RequestParam type(RequestParamType type) {
        this.type = type;
        return this;
    }

    public boolean isOptional() {
        return optional;
    }

    public RequestParam optional(boolean optional) {
        this.optional = optional;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public RequestParam comment(String comment) {
        this.comment = comment;
        return this;
    }
}

