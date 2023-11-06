package com.coffeeshop.model.event;

import com.coffeeshop.model.order.Order;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * Класс {@code OrderStartedEvent} представляет событие начала обработки заказа.
 * При применении этого события к заказу, статус заказа устанавливается как "STARTED".
 * Этот класс не содержит дополнительных полей, отличных от его родительского класса {@code OrderEvent}.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@DiscriminatorValue(EventType.Constants.STARTED)
@Entity
public class OrderStartedEvent extends OrderEvent {

    /**
     * Применяет текущее событие к указанному заказу, устанавливая его статус как "STARTED".
     * @param order Заказ, к которому применяется событие.
     * @return Заказ с обновленным статусом.
     */
    @Override
    public Order applyTo(Order order) {
        order.setStatus(EventType.STARTED);
        return order;
    }
}
