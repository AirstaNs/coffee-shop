package com.coffeeshop.model.event;

import com.coffeeshop.config.hibernate.FormatMapperCustom;
import com.coffeeshop.error.SerializationException;
import com.coffeeshop.model.order.Order;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;


/**
 * Класс {@code OrderRegisteredEvent} представляет событие регистрации заказа.
 * При применении этого события к заказу, статус заказа устанавливается как "REGISTERED".
 * Этот класс также содержит дополнительные поля, такие как {@code clientId}, {@code expectedPickupTime},
 * {@code productId} и {@code productCost}, которые сериализуются и десериализуются при сохранении и загрузке
 * события из базы данных.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@DiscriminatorValue(EventType.Constants.REGISTERED)
@Entity
public class OrderRegisteredEvent extends OrderEvent {

    @Transient
    @NotNull
    @Positive
    private Integer clientId;

    @Transient
    @NotNull
    @JsonFormat(pattern = "HH:mm")
    private LocalTime expectedPickupTime;

    @Transient
    @NotNull
    @Positive
    private Integer productId;

    @Transient
    @NotNull
    @Positive
    private Double productCost;

    /**
     * Проверяет, можно ли применить текущее событие к указанному заказу,
     * что в БД ещё нету заказа с таким Id.
     * Для события регистрации это возможно, если заказ еще не имеет статуса.
     * @param order Заказ, к которому предполагается применить событие.
     * @return {@code true}, если событие можно применить; иначе {@code false}.
     */
    @Override
    public boolean isApplicable(Order order) {
        return order != null && order.getStatus() == null;
    }

    /**
     * Применяет текущее событие к указанному заказу,
     * устанавливая его статус как "REGISTERED".
     * Инициализирует в сервисе новую запись order.
     * @param order Заказ, к которому применяется событие.
     * @return Заказ с обновленным статусом.
     */
    @Override
    public Order applyTo(Order order) {
        order.setStatus(EventType.REGISTERED);
        return order;
    }

    /**
     * Сериализует дополнительные поля события в строку JSON перед сохранением в базе данных.
     */
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

    /**
     * Десериализует строку JSON в дополнительные поля события после загрузки из базы данных.
     */
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