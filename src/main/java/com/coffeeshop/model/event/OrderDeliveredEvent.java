package com.coffeeshop.model.event;

import com.coffeeshop.model.order.Order;

public class OrderDeliveredEvent extends OrderEvent {
    @Override
    public boolean isApplicable(Order order) {
        return super.isApplicable(order) && order.getStatus() != EventType.DELIVERED;
    }

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
