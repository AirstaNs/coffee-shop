package com.coffeeshop.model.event;

import com.coffeeshop.model.order.Order;

import java.time.LocalDateTime;

public abstract class OrderEvent {
    private int id;
    private Long id;

    protected Long orderId;

    protected Long employeeId;

    private LocalDateTime creationDate = LocalDateTime.now();

    public abstract void applyTo(Order order);
}
