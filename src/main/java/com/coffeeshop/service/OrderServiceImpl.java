package com.coffeeshop.service;


import com.coffeeshop.model.event.EventType;
import com.coffeeshop.model.event.OrderEvent;
import com.coffeeshop.model.order.Order;
import com.coffeeshop.repository.OrderEventRepository;
import com.coffeeshop.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderEventRepository eventRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderEventRepository eventRepository) {
        this.orderRepository = orderRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public void publishEvent(OrderEvent event) {
        EventType eventType = event.getEventType();
        Objects.requireNonNull(eventType);
        Order order = null;

        if (eventType == EventType.REGISTERED) {
            order = new Order();
            order.addEvent(event);
        } else if (eventType == EventType.CANCELLED) {
            order = findOrder(event.getOrder().getId());
        }

        // Сохранение заказа и события в базе данных
        order.setStatus(event.getEventType());
        eventRepository.save(event);
        orderRepository.save(order);
    }

    @Override
    public Order findOrder(int id) {
        return orderRepository.getReferenceById(id);
    }
}
