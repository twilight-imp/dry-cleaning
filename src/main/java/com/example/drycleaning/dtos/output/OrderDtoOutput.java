package com.example.drycleaning.dtos.output;

import com.example.drycleaning.dtos.input.OrderDtoInput;
import com.example.drycleaning.models.OrderStatus;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class OrderDtoOutput extends OrderDtoInput implements Serializable {
    private String id;

    private LocalDate date;

    private OrderStatus orderStatus;

    private  Integer finalPrice;

    private String branchId;

    private Boolean isDeleted;

    private List<OrderPositionDtoOutput> orderPosition;

    public OrderDtoOutput(String userId) {
        super(userId);
    }

    public OrderDtoOutput(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    public Integer getFinalPrice() {
        return finalPrice;
    }


    public void setFinalPrice(Integer finalPrice) {
        this.finalPrice = finalPrice;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<OrderPositionDtoOutput> getOrderPosition() {
        return orderPosition;
    }

    public void setOrderPosition(List<OrderPositionDtoOutput> orderPosition) {
        this.orderPosition = orderPosition;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
