package com.coffeeshop.model.event;

import com.coffeeshop.model.order.Order;
import com.coffeeshop.model.order.OrderStatus;
import lombok.Data;

import java.time.LocalTime;

@Data
class OrderRegisteredEvent extends OrderEvent {
    private Long clientId;
    private LocalTime expectedPickupTime;
    private Long productId;
    private Double  productCost;

    @Override
    public void applyTo(Order order) {
        order.setStatus(OrderStatus.REGISTERED);
    }
}