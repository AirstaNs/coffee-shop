package com.coffeeshop.model.event;

import com.coffeeshop.config.hibernate.FormatMapperCustom;
import com.coffeeshop.error.SerializationException;
import com.coffeeshop.model.order.Order;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
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
    private Integer clientId;
    @Transient
    @JsonFormat(pattern = "HH:mm")
    private LocalTime expectedPickupTime;
    @Transient
    private Integer productId;
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

    @PostLoad
    public void deserializeEventData() {
        ObjectMapper mapper = FormatMapperCustom.getObjectMapper();
        try {
            Map<String, Object> eventDataMap = mapper.readValue(this.getEventData(), new TypeReference<>() {});
            this.clientId = (Integer) eventDataMap.get("clientId");
            this.expectedPickupTime = LocalTime.parse((String) eventDataMap.get("expectedPickupTime"));
            this.productId = (Integer) eventDataMap.get("productId");
            this.productCost = (Double) eventDataMap.get("productCost");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка десериализации данных события", e);
        }
    }
}