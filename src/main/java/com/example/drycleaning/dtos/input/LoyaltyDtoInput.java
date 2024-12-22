package com.example.drycleaning.dtos.input;

import java.io.Serializable;

public class LoyaltyDtoInput implements Serializable {
    private  String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
