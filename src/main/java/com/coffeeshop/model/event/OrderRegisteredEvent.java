package com.coffeeshop.model.event;

import com.coffeeshop.config.hibernate.FormatMapperCustom;
import com.coffeeshop.model.order.Order;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
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

    @Override
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
            throw new RuntimeException("Ошибка сериализации данных события", e);
        }
    }

    @Override
    public void deserializeEventData() {
        ObjectMapper mapper = FormatMapperCustom.getObjectMapper();
        try {
            Map<String, Object> eventDataMap = mapper.readValue(this.getEventData(), new TypeReference<>() {});
            this.clientId = (Long) eventDataMap.get("clientId");
            this.expectedPickupTime = LocalTime.parse((String) eventDataMap.get("expectedPickupTime"));
            this.productId = (Long) eventDataMap.get("productId");
            this.productCost = (Double) eventDataMap.get("productCost");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка десериализации данных события", e);
        }
    }
}