package com.coffeeshop.service;


import com.coffeeshop.model.order.Order;
import com.coffeeshop.model.event.OrderEvent;

public interface OrderService {

    void publishEvent(OrderEvent event);

    Order findOrder(int id);
}