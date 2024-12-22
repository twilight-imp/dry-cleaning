package com.example.drycleaning.dtos.input;

import java.io.Serializable;

public class CityDtoInput implements Serializable {
    String name;

    public CityDtoInput(String name) {
        this.name = name;
    }

    public CityDtoInput() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
