package com.coffeeshop.error;

/**
 * Исключение, представляющее ситуацию, когда событие не может быть применено к заказу.
 * <p>
 * Это исключение выбрасывается, когда пытаются применить событие к заказу, но по каким-то причинам
 * это действие недопустимо. Например, пытаются применить событие "Заказ отменен" к уже выданному заказу.
 * </p>
 */
public class EventNotApplicableException extends RuntimeException {
    /**
     * Конструктор исключения с сообщением о причине.
     * @param message Сообщение, описывающее причину исключения.
     */
    public EventNotApplicableException(String message) {
        super(message);
    }
}
