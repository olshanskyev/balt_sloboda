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

    public RequestParam request(RequestType requestType) {
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
}

