package com.coffeeshop.error;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EventNotApplicableException.class)
    public ResponseEntity<ErrorResponse> handleEventNotApplicableException(EventNotApplicableException ex,
            HttpServletRequest request) {
        String path = request.getServletPath();
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, path, ex.getMessage());
        log.error("EventNotApplicableException: {}", errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorResponse> handleJsonParseException(HttpServletRequest request) {
        String msg = "Неверный формат JSON";
        String path = request.getServletPath();
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, path, msg);
        log.error("HttpMessageNotReadableException: {}", errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SerializationException.class)
    public ResponseEntity<ErrorResponse> handleSerializationException(SerializationException ex,
            HttpServletRequest request) {
        String path = request.getServletPath();
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, path, ex.getMessage());
        log.error("SerializationException: {}", errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
