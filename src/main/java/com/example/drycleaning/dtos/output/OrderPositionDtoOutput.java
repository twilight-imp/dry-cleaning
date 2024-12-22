package com.example.drycleaning.dtos.output;

import com.example.drycleaning.dtos.input.OrderPositionDtoInput;

import java.io.Serializable;

public class OrderPositionDtoOutput extends OrderPositionDtoInput implements Serializable {

    private String id;

    private Integer amount;

    private Boolean isDeleted;

    public OrderPositionDtoOutput(){}


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
