package com.coffeeshop.service;


import com.coffeeshop.error.EventNotApplicableException;
import com.coffeeshop.error.OrderNotFoundException;
import com.coffeeshop.model.event.OrderEvent;
import com.coffeeshop.model.order.Order;
import com.coffeeshop.repository.OrderEventRepository;
import com.coffeeshop.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;


/**
 * Сервис {@code OrderServiceImpl} предоставляет реализацию интерфейса {@link OrderService} для обработки заказов и связанных с ними событий.
 *
 * Основные функции сервиса:
 * <ul>
 *     <li>Публикация событий, связанных с заказами.</li>
 *     <li>Получение информации о заказе, включая все связанные события.</li>
 * </ul>
 *
 * При публикации события выполняются следующие проверки:
 * <ul>
 *     <li>Любому из событий должно предшествовать событие регистрации заказа.</li>
 *     <li>Если заказ уже выдан или отменен, публикация новых событий недоступна.</li>
 * </ul>
 * Если все условия соблюдены, событие записывается в базу данных.
 *
 * При получении информации о заказе, возвращается агрегированная информация на основе всех связанных событий.
 * В частности, в модели отражен текущий статус (этап) заказа и присутствует список событий с информацией о типе событий и времени его создания.
 */

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

    /**
     * Публикует событие, связанное с заказом.
     * Перед публикацией проверяет, может ли событие быть применено к заказу.
     * Если событие применимо, изменяет статус заказа и сохраняет событие в базе данных.
     *
     * @param event событие, связанное с заказом.
     * @throws EventNotApplicableException если событие не может быть применено к заказу.
     */
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
            throw new EventNotApplicableException("Event cannot be applied: " + event.getEventType());
        }
    }

    /**
     * Возвращает заказ по его идентификатору вместе со всеми связанными событиями.
     *
     * @param id идентификатор заказа.
     * @return заказ с агрегированной информацией о связанных событиях.
     * @throws OrderNotFoundException если заказ не найден.
     */
    @Override
    public Order findOrder(int id) {
        return orderRepository.findByIdWithEvents(id)
                              .orElseThrow(() -> new OrderNotFoundException("Не найдено заказа: "+ id));
    }
}
