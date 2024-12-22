package com.example.drycleaning.errors;

public class AddUserException extends RuntimeException{
    public AddUserException(String message) {
        super(message);
    }
}
