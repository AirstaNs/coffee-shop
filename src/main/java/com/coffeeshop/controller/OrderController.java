package com.coffeeshop.controller;

import com.coffeeshop.model.event.OrderEvent;
import com.coffeeshop.model.order.Order;
import com.coffeeshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * Контроллер для управления заказами.
 * <p>
 * Этот контроллер предоставляет функциональность для публикации событий заказов и получения информации о заказе.
 * Каждый заказ может пройти через различные этапы, такие как регистрация, принятие в работу, отмена, готовность к выдаче и выдача.
 * Переход заказа на каждый этап сопровождается генерацией соответствующего события.
 * </p>
 * <p>
 * Система реализует подход event sourcing, где заказы хранятся в виде цепочки событий.
 * Этот подход был выбран из-за его гибкости и возможности анализа трудозатрат и выявления проблем в скорости обслуживания клиентов.
 * </p>
 */
@RestController("/orders")
public class OrderController {
    private final OrderService orderService;

    /**
     * Конструктор для инъекции зависимости OrderService.
     *
     * @param orderService Сервис для управления заказами.
     */
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Метод для публикации события заказа.
     * <p>
     * Перед публикацией события выполняется проверка условий:
     * - Любому из событий должно предшествовать событие регистрации заказа.
     * - Если заказ уже выдан или отменен, публикация новых событий недоступна.
     * </p>
     *
     * @param event деморализуется в свой подкласс.
     * @return ResponseEntity с HTTP-статусом - OK или перехват исключений в @ControllerAdvice.
     */
    @PostMapping("/events")
    public ResponseEntity<Void> publishEvent(@RequestBody OrderEvent event) {

        orderService.publishEvent(event);
        return ResponseEntity.ok().build();
    }

    /**
     * Метод для получения информации о заказе.
     * <p>
     * Возвращает полную информацию о заказе на основе агрегации имеющихся событий.
     * В модели отражен текущий статус заказа и список событий с информацией о типе события и времени его создания.
     * </p>
     *
     * @param orderId Идентификатор заказа.
     * @return ResponseEntity с информацией о заказе или HTTP-статусом 404 "не найдено".
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> findOrder(@PathVariable Integer orderId) {
        Order order = orderService.findOrder(orderId);

        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
