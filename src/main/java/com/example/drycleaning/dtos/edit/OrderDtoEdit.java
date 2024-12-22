package com.example.drycleaning.dtos.edit;


import java.io.Serializable;

public class OrderDtoEdit implements Serializable {
    private String orderStatus;

    private String branchId;

    public OrderDtoEdit(String orderStatus, String branchId) {
        this.orderStatus = orderStatus;
        this.branchId = branchId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }
}
