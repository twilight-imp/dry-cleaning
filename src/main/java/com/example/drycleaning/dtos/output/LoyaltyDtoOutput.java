package com.example.drycleaning.dtos.output;

import com.example.drycleaning.dtos.input.LoyaltyDtoInput;

import java.io.Serializable;

public class LoyaltyDtoOutput extends LoyaltyDtoInput implements Serializable {

    private String id;

    private int discount;

    private int numOrders;

    private Boolean isDeleted;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getNumOrders() {
        return numOrders;
    }

    public void setNumOrders(int numOrders) {
        this.numOrders = numOrders;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
