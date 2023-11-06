package com.coffeeshop.error;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;


/**
 * Класс представляет собой структуру ответа об ошибке.
 * <p>
 * Используется для предоставления информации о HTTP-статусе, пути запроса и сообщении об ошибке при возникновении исключений или ошибок во время выполнения запроса.
 * </p>
 */
@Schema(name = "ErrorResponse", description = "Модель ответа об ошибке.")
public record ErrorResponse(
        @Schema(description = "HTTP-статус ошибки.")
        HttpStatus status,
        @Schema(description = "Путь запроса, при выполнении которого произошла ошибка.")
        String path,
        @Schema(description = "Сообщение об ошибке.") String message) {}
