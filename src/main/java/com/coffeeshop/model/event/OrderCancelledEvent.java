package com.coffeeshop.model.event;

import com.coffeeshop.config.hibernate.FormatMapperCustom;
import com.coffeeshop.model.order.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@DiscriminatorValue(EventType.Constants.CANCELLED)
@Entity
public class OrderCancelledEvent extends OrderEvent {

    @Transient
    private String cancelReason;


    @Override
    public Order applyTo(Order order) {
        order.setStatus(EventType.CANCELLED);
        return order;
    }

    @PrePersist
    @PreUpdate
    public void serializeEventData() {
        ObjectMapper mapper = FormatMapperCustom.getObjectMapper();
        try {
            Map<String, Object> eventDataMap = new HashMap<>();
            eventDataMap.put("cancelReason", this.cancelReason);
            this.setEventData(mapper.writeValueAsString(eventDataMap));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка сериализации данных события", e);
        }
    }

    @PostLoad
    public void deserializeEventData() {
        ObjectMapper mapper = FormatMapperCustom.getObjectMapper();
        try {
            Map<String, Object> eventDataMap = mapper.readValue(this.getEventData(), new TypeReference<>() {});
            this.cancelReason = (String) eventDataMap.get("cancelReason");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка десериализации данных события", e);
        }
    }
}