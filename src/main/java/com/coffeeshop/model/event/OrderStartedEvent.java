package com.coffeeshop.model.event;

import com.coffeeshop.model.order.Order;
import lombok.Data;

@Data
public class OrderStartedEvent extends OrderEvent {
    @Override
    public Order applyTo(Order order) {
        order.setStatus(EventType.STARTED);
        return  order;
    }

    @Override
    public void serializeEventData() {

    }

    @Override
    public void deserializeEventData() {

    }
}
