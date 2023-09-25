package com.coffeeshop.model.event;

import com.coffeeshop.model.order.Order;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@DiscriminatorValue(EventType.Constants.STARTED)
@Entity
public class OrderStartedEvent extends OrderEvent {
    @Override
    public Order applyTo(Order order) {
        order.setStatus(EventType.STARTED);
        return order;
    }
}
