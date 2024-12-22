package com.example.drycleaning.dtos.input;

import java.io.Serializable;

public class ServiceDtoInput implements Serializable {

    private String name;

    private String description;

    private Integer cost;

    public ServiceDtoInput(){}

    public ServiceDtoInput(String name, String description, Integer cost) {
        this.name = name;
        this.description = description;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }
}
