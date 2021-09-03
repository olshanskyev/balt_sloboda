package balt.sloboda.portal.model.request;

import balt.sloboda.portal.model.converter.RolesToStringSetConverter;
import balt.sloboda.portal.model.converter.StringToStringSetConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    @Column(name="DEFAULT_VALUE", columnDefinition="varchar(256)", nullable = true)
    private String defaultValue = "";

    @Column(name="COMMENT", columnDefinition="varchar(128)", nullable = true)
    private String comment;

    @Column(name="ENUM_VALUES", columnDefinition="varchar(512)", nullable = true)
    @Convert(converter = StringToStringSetConverter.class)
    private Set<String> enumValues = new HashSet<>(); // enum elements for request param with typ RequestParamType.ENUM


    public Long getId() {
        return id;
    }

    public RequestParam setId(Long id) {
        this.id = id;
        return this;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public RequestParam setRequestType(RequestType requestType) {
        this.requestType = requestType;
        return this;
    }

    public String getName() {
        return name;
    }

    public RequestParam setName(String name) {
        this.name = name;
        return this;
    }

    public RequestParamType getType() {
        return type;
    }

    public RequestParam setType(RequestParamType type) {
        this.type = type;
        return this;
    }

    public boolean isOptional() {
        return optional;
    }

    public RequestParam setOptional(boolean optional) {
        this.optional = optional;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public RequestParam setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public RequestParam setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public Set<String> getEnumValues() {
        return enumValues;
    }

    public RequestParam setEnumValues(Set<String> enumValues) {
        this.enumValues = enumValues;
        return this;
    }
}

