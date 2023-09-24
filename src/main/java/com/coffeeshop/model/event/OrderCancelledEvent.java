package com.coffeeshop.model.event;

import com.coffeeshop.model.order.Order;
import jakarta.persistence.DiscriminatorValue;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
@DiscriminatorValue("CANCELLED")
public class OrderCancelledEvent extends OrderEvent {

    private String cancelReason;

    @Override
    public void applyTo(Order order) {
        order.setStatus(EventType.CANCELLED);
    }
}