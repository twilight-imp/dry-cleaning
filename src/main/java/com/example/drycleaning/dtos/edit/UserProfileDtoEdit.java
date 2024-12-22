package com.example.drycleaning.dtos.edit;

import com.example.drycleaning.validation.UniqueEmail;
import com.example.drycleaning.validation.UniquePhone;

import java.io.Serializable;

public class UserProfileDtoEdit implements Serializable {

    private String lastName;
    private String name;
    @UniquePhone
    private String phone;
    @UniqueEmail
    private String email;
    private String password;

    private String newPassword;
    private String city;


    public UserProfileDtoEdit(){}

    public UserProfileDtoEdit(String lastName, String name, String phone, String email, String password, String newPassword, String city) {
        this.lastName = lastName;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.newPassword = newPassword;
        this.city = city;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
