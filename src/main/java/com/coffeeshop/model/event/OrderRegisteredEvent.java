package com.coffeeshop.model.event;

import com.coffeeshop.model.order.Order;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalTime;

@EqualsAndHashCode(callSuper = true)
@Data
class OrderRegisteredEvent extends OrderEvent {
    private Long clientId;
    private LocalTime expectedPickupTime;
    private Long productId;
    private Double productCost;

    @Override
    public void applyTo(Order order) {
        order.setStatus(EventType.REGISTERED);
    }
}