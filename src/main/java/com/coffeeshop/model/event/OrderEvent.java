package com.coffeeshop.model.event;

import com.coffeeshop.model.order.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


@Data
@Entity
@Table(name = "event")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "event_type", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, visible = true, property = "event_type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = OrderRegisteredEvent.class, name = EventType.Constants.REGISTERED),
        @JsonSubTypes.Type(value = OrderCancelledEvent.class, name = EventType.Constants.CANCELLED),
        @JsonSubTypes.Type(value = OrderStartedEvent.class, name = EventType.Constants.STARTED),
        @JsonSubTypes.Type(value = OrderCompletedEvent.class, name =EventType.Constants.COMPLETED),
        @JsonSubTypes.Type(value = OrderDeliveredEvent.class, name = EventType.Constants.DELIVERED)
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
    private LocalDateTime creationDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);


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
}
