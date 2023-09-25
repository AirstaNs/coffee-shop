package com.coffeeshop.model.order;


import com.coffeeshop.model.event.EventType;
import com.coffeeshop.model.event.OrderEvent;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "shop_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "order_id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    private EventType status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderEvent> events = new ArrayList<>();

    @JsonCreator
    public Order(@JsonProperty("order_id") Integer id) {
        this.id = id;
    }
}
