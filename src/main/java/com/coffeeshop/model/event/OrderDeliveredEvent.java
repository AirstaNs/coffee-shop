package com.coffeeshop.model.event;

import com.coffeeshop.model.order.Order;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@DiscriminatorValue("DELIVERED")
@Entity
public class OrderDeliveredEvent extends OrderEvent {
    @Override
    public Order applyTo(Order order) {
        order.setStatus(EventType.DELIVERED);
        return order;
    }

    @Override
    public void serializeEventData() {}

    @Override
    public void deserializeEventData() {}
}