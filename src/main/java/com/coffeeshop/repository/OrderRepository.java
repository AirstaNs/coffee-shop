package com.coffeeshop.repository;

import com.coffeeshop.model.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT o FROM Order o JOIN FETCH o.events WHERE o.id = :id")
    Optional<Order> findByIdWithEvents(@Param("id") Integer id);
}