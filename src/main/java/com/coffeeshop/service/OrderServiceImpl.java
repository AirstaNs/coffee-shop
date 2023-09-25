package com.coffeeshop.service;


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
    public void publishEvent(OrderEvent event) throws IllegalStateException {
        Objects.requireNonNull(event);

        Order order = event.getOrder();
        if (order != null && order.getId() != null) {
            order = orderRepository.findById(order.getId()).orElse(null);
        }

        if (event.isApplicable(order)) {
            order = event.applyTo(order);// Изменяем заказ при необходимости (статус)

            // Сохраняем изменения в заказе (статус) если оно ещё не зарегистрировано и сохраняем событие.
            orderRepository.save(Objects.requireNonNull(order));
            eventRepository.save(event);
        } else {
            throw new IllegalStateException("Event cannot be applied: " + event.getClass().getSimpleName());
        }
    }

    @Override
    public Order findOrder(int id) {
        return orderRepository.getReferenceById(id);
    }
}
