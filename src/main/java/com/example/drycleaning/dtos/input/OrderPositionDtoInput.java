package com.example.drycleaning.dtos.input;

import java.io.Serializable;

public class OrderPositionDtoInput implements Serializable {

    private String product;

    private String service;

    private String order;

    private Integer number;

    public OrderPositionDtoInput(String product, String service, String order, Integer number) {
        this.product = product;
        this.service = service;
        this.order = order;
        this.number = number;
    }
    public OrderPositionDtoInput(){}

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
