package com.coffeeshop.error;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * Глобальный обработчик исключений для контроллеров приложения.
 * <p>
 * Этот класс обрабатывает различные исключения, которые могут возникнуть во время выполнения приложения,
 * и возвращает соответствующий ответ с информацией об ошибке.
 * </p>
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Обработка исключений, связанных с неприменимыми событиями.
     *
     * @param ex Исключение.
     * @param request Запрос.
     * @return Ответ с информацией об ошибке.
     */
    @ExceptionHandler(EventNotApplicableException.class)
    public ResponseEntity<ErrorResponse> handleEventNotApplicableException(EventNotApplicableException ex,
            HttpServletRequest request) {
        String path = request.getServletPath();
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, path, ex.getMessage());
        log.error("EventNotApplicableException: {}", errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    /**
     * Обработка исключений, связанных с неверным форматом JSON.
     *
     * @param request Запрос.
     * @return Ответ с информацией об ошибке.
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorResponse> handleJsonParseException(HttpServletRequest request) {
        String msg = "Неверный формат JSON";
        String path = request.getServletPath();
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, path, msg);
        log.error("HttpMessageNotReadableException: {}", errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    /**
     * Обработка исключений, связанных с сериализацией.
     *
     * @param ex Исключение.
     * @param request Запрос.
     * @return Ответ с информацией об ошибке.
     */
    @ExceptionHandler(SerializationException.class)
    public ResponseEntity<ErrorResponse> handleSerializationException(SerializationException ex,
            HttpServletRequest request) {
        String path = request.getServletPath();
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, path, ex.getMessage());
        log.error("SerializationException: {}", errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Обработка исключений, связанных с ненайденным заказом.
     *
     * @param ex Исключение.
     * @param request Запрос.
     * @return Ответ с информацией об ошибке.
     */
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderNotFoundException(OrderNotFoundException ex,
            HttpServletRequest request) {
        String path = request.getServletPath();
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, path, ex.getMessage());
        log.error("OrderNotFoundException: {}", errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
