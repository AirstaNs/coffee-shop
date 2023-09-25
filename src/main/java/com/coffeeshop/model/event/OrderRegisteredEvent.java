package com.coffeeshop.model.event;

import com.coffeeshop.config.hibernate.FormatMapperCustom;
import com.coffeeshop.error.SerializationException;
import com.coffeeshop.model.order.Order;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@DiscriminatorValue(EventType.Constants.REGISTERED)
@Entity
public class OrderRegisteredEvent extends OrderEvent {
    @Transient
    private Long clientId;
    @Transient
    @JsonFormat(pattern = "HH:mm")
    private LocalTime expectedPickupTime;
    @Transient
    private Long productId;
    @Transient
    private Double productCost;

    @Override
    public boolean isApplicable(Order order) {
        return order != null && order.getStatus() == null;
    }

    @Override
    public Order applyTo(Order order) {
        order.setStatus(EventType.REGISTERED);
        return order;
    }

    @PrePersist
    @PreUpdate
    public void serializeEventData() {
        ObjectMapper mapper = FormatMapperCustom.getObjectMapper();
        try {
            Map<String, Object> eventDataMap = new HashMap<>();
            eventDataMap.put("clientId", this.clientId);
            eventDataMap.put("expectedPickupTime", this.expectedPickupTime);
            eventDataMap.put("productId", this.productId);
            eventDataMap.put("productCost", this.productCost);
            this.setEventData(mapper.writeValueAsString(eventDataMap));
        } catch (JsonProcessingException e) {
            throw new SerializationException("Ошибка сериализации данных события");
        }
    }
}