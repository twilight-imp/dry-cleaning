package com.example.drycleaning.models;

public enum OrderStatus implements Comparable<OrderStatus>{
    NEW("Новый"),
    CREATED("Создан"),
    AT_WORK("В работе"),
    READY("Готов"),
    COMPLETED("Завершен"),
    CANCELLED("Отменен");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
