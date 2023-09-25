package com.coffeeshop.model.event;

public enum EventType {
    REGISTERED(Constants.REGISTERED),
    CANCELLED(Constants.CANCELLED),
    STARTED(Constants.STARTED),
    COMPLETED(Constants.COMPLETED),
    DELIVERED(Constants.DELIVERED);

    public final String NAME;

    EventType(String name) {
        this.NAME = name;
    }

    public static class Constants {
        public static final String REGISTERED = "REGISTERED";
        public static final String CANCELLED = "CANCELLED";
        public static final String STARTED = "STARTED";
        public static final String COMPLETED = "COMPLETED";
        public static final String DELIVERED = "DELIVERED";
    }
}

