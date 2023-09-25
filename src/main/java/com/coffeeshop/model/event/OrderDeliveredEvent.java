package com.coffeeshop.model.event;

import com.coffeeshop.model.order.Order;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * Класс {@code OrderDeliveredEvent} представляет событие выдачи заказа.
 * При применении этого события к заказу, статус заказа устанавливается как "DELIVERED".
 */
@EqualsAndHashCode(callSuper = true)
@Data
@DiscriminatorValue(EventType.Constants.DELIVERED)
@Entity
public class OrderDeliveredEvent extends OrderEvent {

    /**
     * Применяет текущее событие к указанному заказу, устанавливая его статус как "DELIVERED".
     *
     * @param order Заказ, к которому применяется событие.
     * @return Заказ с обновленным статусом.
     */
    @Override
    public Order applyTo(Order order) {
        order.setStatus(EventType.DELIVERED);
        return order;
    }
}
