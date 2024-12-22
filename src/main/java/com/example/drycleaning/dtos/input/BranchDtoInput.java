package com.example.drycleaning.dtos.input;

import java.io.Serializable;

public class BranchDtoInput implements Serializable {

    private String name;
    private String city;
    private String address;
    private String phone;
//конструктор, геттеры, сеттеры
    public BranchDtoInput(String name, String city, String address, String phone) {
        this.name = name;
        this.city = city;
        this.address = address;
        this.phone = phone;
    }

    public BranchDtoInput(){};

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
        public String toString() {
            return "BranchDtoInput{" +"name='" + name + ", city='" + city + ", address='" + address + ", phone='" + phone + '}';
    }

}