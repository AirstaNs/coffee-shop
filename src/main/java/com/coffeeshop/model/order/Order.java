package com.coffeeshop.model.order;


import com.coffeeshop.model.event.OrderEvent;
import lombok.Data;

import java.util.List;

@Data
public class Order {

    private int id;
    private OrderStatus status;
    private List<OrderEvent> events;
    public void applyEvents() {
        for (OrderEvent event : events) {
            event.applyTo(this);
        }
    }
}
