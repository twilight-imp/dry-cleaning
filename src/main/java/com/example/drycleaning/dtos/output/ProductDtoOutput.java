package com.example.drycleaning.dtos.output;

import com.example.drycleaning.dtos.input.ProductDtoInput;

import java.io.Serializable;

public class ProductDtoOutput extends ProductDtoInput implements Serializable {

    private String id;
    private Boolean isDeleted;

    public ProductDtoOutput(){}

    public ProductDtoOutput(String name, String material, Integer extraCharge, String typeProductId, String id, Boolean isDeleted) {
        super(name, material, extraCharge, typeProductId);
        this.id = id;
        this.isDeleted = isDeleted;
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
