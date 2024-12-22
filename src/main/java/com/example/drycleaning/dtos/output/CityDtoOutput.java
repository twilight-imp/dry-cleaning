package com.example.drycleaning.dtos.output;

import com.example.drycleaning.dtos.input.CityDtoInput;

import java.io.Serializable;

public class CityDtoOutput extends CityDtoInput implements Serializable {

    String id;

    private Boolean isDeleted;

    public CityDtoOutput(String name, String id, Boolean isDeleted) {
        super(name);
        this.id = id;
        this.isDeleted = isDeleted;
    }

    public CityDtoOutput(String id, Boolean isDeleted) {
        this.id = id;
        this.isDeleted = isDeleted;
    }

    public CityDtoOutput() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
