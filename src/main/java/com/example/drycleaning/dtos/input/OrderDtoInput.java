package com.example.drycleaning.dtos.input;

import com.example.drycleaning.dtos.output.OrderPositionDtoOutput;

import java.io.Serializable;
import java.util.Set;

public class OrderDtoInput implements Serializable {

    private String userId;

    public OrderDtoInput(String userId) {
        this.userId = userId;
    }

    public OrderDtoInput(){}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
