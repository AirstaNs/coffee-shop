package com.coffeeshop.model.event;

import com.coffeeshop.config.hibernate.FormatMapperCustom;
import com.coffeeshop.model.order.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;


/**
 * Класс {@code OrderCancelledEvent} представляет событие отмены заказа.
 * Содержит причину отмены заказа и методы для сериализации и десериализации данных события.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@DiscriminatorValue(EventType.Constants.CANCELLED)
@Entity
public class OrderCancelledEvent extends OrderEvent {

    /**
     * этот аргумент хранится в json в поле {@link OrderEvent#getEventData()}
     */
    @NotBlank(message = "причина отмены заказа не может быть пустым")
    @Transient
    private String cancelReason;

    /**
     * Применяет текущее событие к указанному заказу, устанавливая его статус как "CANCELLED".
     *
     * @param order Заказ, к которому применяется событие.
     * @return Заказ с обновленным статусом.
     */
    @Override
    public Order applyTo(Order order) {
        order.setStatus(EventType.CANCELLED);
        return order;
    }

    /**
     * Сериализует данные события перед сохранением в базу данных.
     */
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
    /**
     * Десериализует данные события после загрузки из базы данных.
     */
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