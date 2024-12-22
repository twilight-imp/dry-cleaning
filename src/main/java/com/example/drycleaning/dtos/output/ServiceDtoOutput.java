package com.example.drycleaning.dtos.output;

import com.example.drycleaning.dtos.input.ServiceDtoInput;

import java.io.Serializable;

public class ServiceDtoOutput extends ServiceDtoInput implements Serializable {
    private String serviceId;
    private Boolean isDeleted;

    public ServiceDtoOutput(String name, String description, Integer cost) {
        super(name, description, cost);
    }

    public ServiceDtoOutput(String id,String name, String description, Integer cost, Boolean isDeleted) {
        super(name, description, cost);
        this.serviceId = id;
        this.isDeleted = isDeleted;
    }

    public ServiceDtoOutput(){}

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String id) {
        this.serviceId = id;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
