package com.coffeeshop.model.event;

import com.coffeeshop.model.order.Order;

public class OrderDeliveredEvent extends OrderEvent{
    @Override
    public void applyTo(Order order) {
        order.setStatus(EventType.DELIVERED);
    }

    @Override
    public void serializeEventData() {}

    @Override
    public void deserializeEventData() {}
}
