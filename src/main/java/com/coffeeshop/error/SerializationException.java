package com.coffeeshop.error;

/**
 * Исключение, связанное с ошибками сериализации.
 * <p>
 * Это исключение выбрасывается, когда возникают проблемы при попытке сериализовать или десериализовать объекты.
 * </p>
 */
public class SerializationException extends RuntimeException {
    /**
     * Конструктор для создания нового экземпляра исключения с указанным сообщением.
     * @param message Сообщение об ошибке.
     */
    public SerializationException(String message) {
        super(message);
    }
}
