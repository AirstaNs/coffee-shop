package com.coffeeshop.model.event;

import com.coffeeshop.model.order.Order;

public class OrderCompletedEvent extends OrderEvent{
    @Override
    public void applyTo(Order order) {
        order.setStatus(EventType.COMPLETED);
    }

    @Override
    public void serializeEventData() {

    }

    @Override
    public void deserializeEventData() {

    }
}
