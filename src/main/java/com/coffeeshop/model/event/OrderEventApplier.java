package com.coffeeshop.model.event;

import com.coffeeshop.model.order.Order;

public interface OrderEventApplier {
    boolean isApplicable(Order order);
    Order applyTo(Order order);
}
