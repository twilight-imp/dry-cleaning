package com.example.drycleaning.dtos.output;

import com.example.drycleaning.dtos.input.TypeProductDtoInput;

import java.io.Serializable;

public class TypeProductDtoOutput extends TypeProductDtoInput implements Serializable {

    private String id;

    private Boolean isDeleted;


    public TypeProductDtoOutput(String name) {
        super(name);
    }

    public TypeProductDtoOutput() {
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
