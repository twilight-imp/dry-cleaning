package com.example.drycleaning.models;


public enum UserRoles {
    USER("Пользователь"),
    MODERATOR("Модератор"),
    ADMIN("Администратор");

    private final String description;

    UserRoles(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
