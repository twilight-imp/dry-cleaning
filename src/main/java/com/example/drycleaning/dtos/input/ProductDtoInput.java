package com.example.drycleaning.dtos.input;

import java.io.Serializable;

public class ProductDtoInput implements Serializable {

    private String name;

    private String material;

    private Integer extraCharge;

    private String typeProductId;

    public ProductDtoInput(){}

    public ProductDtoInput(String name, String material, Integer extraCharge, String typeProductId) {
        this.name = name;
        this.material = material;
        this.extraCharge = extraCharge;
        this.typeProductId = typeProductId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public Integer getExtraCharge() {
        return extraCharge;
    }

    public void setExtraCharge(Integer extraCharge) {
        this.extraCharge = extraCharge;
    }

    public String getTypeProductId() {
        return typeProductId;
    }

    public void setTypeProductId(String typeProductId) {
        this.typeProductId = typeProductId;
    }
}
