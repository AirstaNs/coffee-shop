package com.coffeeshop.model.event;

import com.coffeeshop.model.order.Order;
import lombok.Data;

@Data
public class OrderStartedEvent extends OrderEvent {
    @Override
    public void applyTo(Order order) {
        order.setStatus(EventType.STARTED);
    }

    @Override
    public void serializeEventData() {

    }

    @Override
    public void deserializeEventData() {

    }
}
