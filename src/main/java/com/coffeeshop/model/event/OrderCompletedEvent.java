package com.coffeeshop.model.event;

import com.coffeeshop.model.order.Order;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * Класс {@code OrderCompletedEvent} представляет событие завершения заказа.
 * При применении этого события к заказу, статус заказа устанавливается как "COMPLETED".
 */
@EqualsAndHashCode(callSuper = true)
@Data
@DiscriminatorValue(EventType.Constants.COMPLETED)
@Entity
public class OrderCompletedEvent extends OrderEvent {
    /**
     * Применяет текущее событие к указанному заказу, устанавливая его статус как "COMPLETED".
     * @param order Заказ, к которому применяется событие.
     * @return Заказ с обновленным статусом.
     */
    @Override
    public Order applyTo(Order order) {
        order.setStatus(EventType.COMPLETED);
        return order;
    }
}
