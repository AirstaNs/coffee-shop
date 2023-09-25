package com.coffeeshop.repository;

import com.coffeeshop.model.event.OrderEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий {@code OrderEventRepository} предоставляет методы для работы с сущностями {@link OrderEvent} в базе данных.
 * Этот репозиторий расширяет {@link JpaRepository}, что предоставляет стандартные методы CRUD для сущности {@link OrderEvent}.
 */
@Repository
public interface OrderEventRepository extends JpaRepository<OrderEvent, Integer> {
}

