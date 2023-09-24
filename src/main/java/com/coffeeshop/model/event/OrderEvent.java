package com.coffeeshop.model.event;

import com.coffeeshop.model.order.Order;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "event")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, visible = true, property = "event_type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = OrderRegisteredEvent.class, name = "REGISTERED"),
        @JsonSubTypes.Type(value = OrderCancelledEvent.class, name = "CANCELLED")
})
public abstract class OrderEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    protected Long employeeId;

    @Column(name = "creation_date")
    private LocalDateTime creationDate = LocalDateTime.now();


    @Column(name = "event_type")
    @JsonProperty("event_type")
    @Enumerated(EnumType.STRING)
    private  EventType eventType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "data")
    private String eventData;

    public abstract void applyTo(Order order);
}
