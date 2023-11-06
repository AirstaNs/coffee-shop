package com.coffeeshop.error;

/**
 * Исключение, указывающее на отсутствие заказа.
 * <p>
 * Это исключение выбрасывается, когда заказ, который пытаются найти, отсутствует в системе.
 * </p>
 */
public class OrderNotFoundException extends RuntimeException {
    /**
     * Конструктор для создания нового экземпляра исключения с указанным сообщением.
     * @param message Сообщение об ошибке.
     */
    public OrderNotFoundException(String message) {
        super(message);
    }
}