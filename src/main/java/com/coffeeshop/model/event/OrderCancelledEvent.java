package com.coffeeshop.model.event;

import com.coffeeshop.model.order.Order;
import com.coffeeshop.model.order.OrderStatus;

public class OrderCancelledEvent extends OrderEvent {

    private String cancelReason;

    @Override
    public void applyTo(Order order) {
        order.setStatus(OrderStatus.CANCELLED);
    }
}