package com.coffeeshop.model.event;

import com.coffeeshop.model.order.Order;


/**
 * Интерфейс {@code OrderEventApplier} определяет контракт для событий, которые могут быть применены к заказу.
 * Этот интерфейс предоставляет методы для проверки применимости события к заказу и для применения события к заказу.
 */
public interface OrderEventApplier {

    /**
     * Проверяет, может ли текущее событие быть применено к указанному заказу.
     * @param order Заказ, к которому предполагается применить событие.
     * @return {@code true}, если событие может быть применено к заказу; иначе {@code false}.
     */
    boolean isApplicable(Order order);

    /**
     * Применяет текущее событие к указанному заказу.
     * @param order Заказ, к которому применяется событие.
     * @return Заказ с обновленным состоянием после применения события.
     */
    Order applyTo(Order order);
}
