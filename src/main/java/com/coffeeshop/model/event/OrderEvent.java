package com.coffeeshop.model.event;

import com.coffeeshop.model.order.Order;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


/**
 * Базовый класс {@code OrderEvent} представляет события, связанные с заказами.
 * События хранятся в базе данных в виде цепочки (подход event sourcing).
 * Поля подтипов хранятся в json формате  {@link  #eventData}.
 * Jackson десереализует в подтип.
 */
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
@JsonPropertyOrder({"event_id", "event_type", "creation_date", "order_id", "employeeId"})
public abstract class OrderEvent implements OrderEventApplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    @JsonProperty("event_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonUnwrapped
    @JsonBackReference
    private Order order;

    @Column(name = "employee_id", nullable = false)
    @NotNull
    @Positive
    protected Long employeeId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "creation_date")
    private LocalDateTime creationDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

    @JsonProperty("event_type")
    @Column(name = "event_type", insertable = false, updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private EventType eventType;


    /**
     * Поле для из котного и в который маппится поле data в БД типа json/
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "data")
    @JsonIgnore
    private String eventData; // TODO JsonNode  type


    /**
     * Проверяет, можно ли применить данное событие к указанному заказу.
     * Условия:
     * - Любому из событий должно предшествовать событие регистрации заказа.
     * - Если заказ уже выдан или отменен, публикация новых событий недоступна.
     *
     * @param order Заказ, к которому пытаемся применить событие.
     * @return {@code true}, если событие применимо, иначе {@code false}.
     */
    public boolean isApplicable(Order order) {
        if (order == null) return false;

        EventType status = order.getStatus();
        return status != null
               && status != EventType.CANCELLED
               && status != EventType.DELIVERED;
    }
}
