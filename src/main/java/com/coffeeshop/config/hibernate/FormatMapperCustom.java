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

public class FormatMapperCustom implements FormatMapper {
    private final FormatMapper delegate;
    private static final ObjectMapper customObjectMapper = createObjectMapper();


    public FormatMapperCustom() {
        ObjectMapper objectMapper = createObjectMapper();
        delegate = new JacksonJsonFormatMapper(objectMapper);
    }

    public static ObjectMapper getObjectMapper() {
        return customObjectMapper;
    }

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