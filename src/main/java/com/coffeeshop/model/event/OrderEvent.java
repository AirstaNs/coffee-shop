package com.coffeeshop.model.event;

import com.coffeeshop.model.order.Order;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "event")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "event_type", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, visible = true, property = "event_type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = OrderRegisteredEvent.class, name = "REGISTERED"),
        @JsonSubTypes.Type(value = OrderCancelledEvent.class, name = "CANCELLED"),
        @JsonSubTypes.Type(value = OrderStartedEvent.class, name = "STARTED"),
        @JsonSubTypes.Type(value = OrderCompletedEvent.class, name = "COMPLETED"),
        @JsonSubTypes.Type(value = OrderDeliveredEvent.class, name = "DELIVERED")
})
public abstract class OrderEvent implements OrderEventApplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonUnwrapped
    private Order order;

    protected Long employeeId;

    @Column(name = "creation_date")
    private LocalDateTime creationDate = LocalDateTime.now();


    @Column(name = "event_type", insertable = false, updatable = false)
    @JsonProperty("event_type")
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "data")
    @JsonIgnore
    private String eventData;


    public boolean isApplicable(Order order) {
        if (order == null) return false;

        EventType status = order.getStatus();
        return status != null
               && status != EventType.CANCELLED
               && status != EventType.DELIVERED;
    }

    @PrePersist
    @PreUpdate
    public abstract void serializeEventData();

    @PostLoad
    public abstract void deserializeEventData();

}
