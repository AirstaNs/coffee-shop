package com.coffeeshop.repository;

import com.coffeeshop.model.event.OrderEvent;
import com.coffeeshop.model.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Репозиторий {@code OrderRepository} предоставляет методы для работы с сущностями {@link Order} в базе данных.
 * Этот репозиторий расширяет {@link JpaRepository}, что предоставляет стандартные методы CRUD для сущности {@link Order}.
 * <p>
 * Особенность этого репозитория в том, что он предоставляет метод {@link #findByIdWithEvents(Integer)}, который позволяет
 * лениво загрузить все связанные события {@link OrderEvent} для конкретного заказа.
 * </p>
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    /**
     * Загружает заказ по его идентификатору вместе со всеми связанными событиями.
     *
     * @param id идентификатор заказа.
     * @return {@link Optional} содержащий заказ или пустой, если заказ не найден.
     */
    @Query("SELECT o FROM Order o JOIN FETCH o.events WHERE o.id = :id")
    Optional<Order> findByIdWithEvents(@Param("id") Integer id);
}