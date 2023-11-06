package com.coffeeshop.model.event;


/**
 * Перечисление {@code EventType} представляет типы событий, связанных с заказами.
 * <p>
 * Этот класс обеспечивает жесткую привязку к имени и типу события во всем проекте.
 * Он используется в аннотациях {@code OrderEvent} и хранится в поле {@code EventType eventType} класса {@code OrderEvent}.
 * </p>
 * <p>
 * Вложенный класс {@code Constants} предоставляет константы для наименований типов событий, что позволяет обеспечить
 * централизованное управление именами событий и их использование в аннотациях и других частях системы.
 * </p>
 */
public enum EventType {
    REGISTERED(Constants.REGISTERED),
    CANCELLED(Constants.CANCELLED),
    STARTED(Constants.STARTED),
    COMPLETED(Constants.COMPLETED),
    DELIVERED(Constants.DELIVERED);

    public final String NAME;


    /**
     * Конструктор для инициализации типа события с заданным именем .
     * @param name Наименование типа события.
     */
    EventType(String name) {
        this.NAME = name;
    }

    /**
     * Вложенный класс, предоставляющий константы для наименований типов событий, реализован для жесткой привязки типа события во всем проекте.
     * Используется в основном в аннотациях.
     */
    public static class Constants {
        public static final String REGISTERED = "REGISTERED";
        public static final String CANCELLED = "CANCELLED";
        public static final String STARTED = "STARTED";
        public static final String COMPLETED = "COMPLETED";
        public static final String DELIVERED = "DELIVERED";
    }
}

