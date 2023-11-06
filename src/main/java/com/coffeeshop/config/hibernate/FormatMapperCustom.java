package com.coffeeshop.config.hibernate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.format.FormatMapper;
import org.hibernate.type.format.jackson.JacksonJsonFormatMapper;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


/**
 * Класс FormatMapperCustom предоставляет кастомный маппер для форматирования и сериализации объектов.
 * <p>
 * Этот маппер используется во всем проекте для обеспечения единообразного форматирования и сериализации объектов.
 * Он предоставляет метод {@link #getObjectMapper()}, который возвращает настроенный экземпляр {@link ObjectMapper}.
 * </p>
 */
public class FormatMapperCustom implements FormatMapper {
    private static final ObjectMapper customObjectMapper = createObjectMapper();
    private final FormatMapper delegate;

    /**
     * Конструктор для создания нового экземпляра кастомного маппера.
     * инициализирует {@link #delegate}
     */
    public FormatMapperCustom() {
        ObjectMapper objectMapper = createObjectMapper();
        delegate = new JacksonJsonFormatMapper(objectMapper);
    }

    /**
     * Получение экземпляра настроенного {@link ObjectMapper}.
     * @return Настроенный экземпляр {@link ObjectMapper}.
     */
    public static ObjectMapper getObjectMapper() {
        return customObjectMapper;
    }

    /**
     * Создать и настроить экземпляр {@link ObjectMapper} для сериализации объектов типа {@link LocalTime} и {@link LocalDateTime}.
     * <p>
     * Этот метод настраивает сериализацию объектов типа {@link LocalTime} с использованием формата "HH:mm" и
     * объектов типа {@link LocalDateTime} с использованием стандартного формата ISO_LOCAL_DATE_TIME.
     * </p>
     * @return Настроенный экземпляр {@link ObjectMapper}.
     */
    private static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm")));

        SimpleModule ldtModule = new SimpleModule();
        ldtModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        objectMapper.registerModule(module);
        objectMapper.registerModule(ldtModule);

        return objectMapper;
    }

    @Override
    public <T> T fromString(CharSequence charSequence, JavaType<T> javaType, WrapperOptions wrapperOptions) {
        return delegate.fromString(charSequence, javaType, wrapperOptions);
    }

    @Override
    public <T> String toString(T t, JavaType<T> javaType, WrapperOptions wrapperOptions) {
        return delegate.toString(t, javaType, wrapperOptions);
    }
}