package com.coffeeshop.model.event;

import com.coffeeshop.model.order.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@DiscriminatorValue("CANCELLED")
@Entity
public class OrderCancelledEvent extends OrderEvent {

    @Transient
    private String cancelReason;

    @Override
    public void applyTo(Order order) {
        order.setStatus(EventType.CANCELLED);
    }

    @Override
    public void serializeEventData() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        try {
            Map<String, Object> eventDataMap = new HashMap<>();
            eventDataMap.put("cancelReason", this.cancelReason);
            this.setEventData(mapper.writeValueAsString(eventDataMap));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка сериализации данных события", e);
        }
    }

    @Override
    public void deserializeEventData() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        try {
            Map<String, Object> eventDataMap = mapper.readValue(this.getEventData(), new TypeReference<>() {});
            this.cancelReason = (String) eventDataMap.get("cancelReason");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка десериализации данных события", e);
        }
    }
}