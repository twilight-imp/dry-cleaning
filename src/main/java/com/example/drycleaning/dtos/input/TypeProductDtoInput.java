package com.example.drycleaning.dtos.input;

import java.io.Serializable;

public class TypeProductDtoInput implements Serializable {
    private String name;

    public TypeProductDtoInput(String name) {
        this.name = name;
    }

    public TypeProductDtoInput() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
