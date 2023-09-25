package com.coffeeshop.model.order;


import com.coffeeshop.model.event.EventType;
import com.coffeeshop.model.event.OrderEvent;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


/**
 * Класс {@code Order} представляет собой агрегат заказа в системе.
 * <p>
 * Заказ в системе представлен в виде цепочки событий, что соответствует подходу event sourcing.
 * Основное преимущество такого подхода - гибкость в плане дальнейшего разбиения заказа на этапы, что может быть необходимо, например, для анализа трудозатрат и выявления проблем в скорости обслуживания клиентов.
 * </p>
 * <p>
 * Важной особенностью класса является то, что он хранит только текущий статус заказа. Полный список событий, связанных с заказом, заполняется только при выборке из базы данных.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "shop_order")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "order_id")
    @JsonProperty("order_id")
    private Integer id;

    /**
     * Текущий статус заказа. Отражает последнее событие, связанное с заказом.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Enumerated(EnumType.STRING)
    private EventType status;

    /**
     * Список всех событий, связанных с заказом.
     * Заполняется только при выборке из базы данных.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonManagedReference
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderEvent> events = new ArrayList<>();

    @JsonCreator
    public Order(@JsonProperty("order_id") Integer id) {
        this.id = id;
    }
}
