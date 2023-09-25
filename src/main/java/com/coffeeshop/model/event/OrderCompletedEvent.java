package com.coffeeshop.model.event;

import com.coffeeshop.model.order.Order;

public class OrderCompletedEvent extends OrderEvent{
    @Override
    public Order applyTo(Order order) {
        order.setStatus(EventType.COMPLETED);
        return  order;
    }

    @Override
    public void serializeEventData() {

    }

    @Override
    public void deserializeEventData() {

    }
}
