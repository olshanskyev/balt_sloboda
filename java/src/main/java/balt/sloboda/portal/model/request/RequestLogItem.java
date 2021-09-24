package balt.sloboda.portal.model.request;

import balt.sloboda.portal.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="REQUESTS_LOG")
public class RequestLogItem {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="REQUEST_ID", nullable = false)
    private Request request;

    @Enumerated(EnumType.STRING)
    @Column(name="ITEM_NAME", nullable = false)
    private RequestLogItemName itemName;

    @Column(name="PREV_VALUE", columnDefinition="varchar(256)", nullable = true)
    private String prevValue;

    @Column(name="NEW_VALUE", columnDefinition="varchar(256)", nullable = true)
    private String newValue;

    @OneToOne
    @JoinColumn(name="MODIFIED_BY_ID", nullable = true)
    private User modifiedBy;

    @Column(name="MODIFIED_DATE")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @CreationTimestamp
    private LocalDateTime modifiedDate;

    public Long getId() {
        return id;
    }

    public RequestLogItem setId(Long id) {
        this.id = id;
        return this;
    }

    public Request getRequest() {
        return request;
    }

    public RequestLogItem setRequest(Request request) {
        this.request = request;
        return this;
    }

    public RequestLogItemName getItemName() {
        return itemName;
    }

    public RequestLogItem setItemName(RequestLogItemName itemName) {
        this.itemName = itemName;
        return this;
    }

    public String getPrevValue() {
        return prevValue;
    }

    public RequestLogItem setPrevValue(String prevValue) {
        this.prevValue = prevValue;
        return this;
    }

    public String getNewValue() {
        return newValue;
    }

    public RequestLogItem setNewValue(String newValue) {
        this.newValue = newValue;
        return this;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public RequestLogItem setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }
}
