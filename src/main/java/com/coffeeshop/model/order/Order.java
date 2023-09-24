package com.coffeeshop.model.order;


import com.coffeeshop.model.event.EventType;
import com.coffeeshop.model.event.OrderEvent;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "order_id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    private EventType status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderEvent> events = new ArrayList<>();
    public void applyEvents() {
        for (OrderEvent event : events) {
            event.applyTo(this);
        }
    @JsonCreator
    public Order(@JsonProperty("order_id") Integer id) {
        this.id = id;
    }
}
