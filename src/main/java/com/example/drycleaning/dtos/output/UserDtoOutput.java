package com.example.drycleaning.dtos.output;

import com.example.drycleaning.models.Role;

import java.io.Serializable;
import java.util.List;

public class UserDtoOutput implements Serializable {
    private String id;

    private String lastName;
    private String name;
    private String phone;
    private String email;
    private String password;

    private String city;

    private Boolean isDeleted;

    private List<Role> roles;


    public UserDtoOutput(String id, String lastName, String name, String phone, String email, String password, String city, Boolean isDeleted, List<Role> roles) {
        this.id = id;
        this.lastName = lastName;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.city = city;
        this.isDeleted = isDeleted;
        this.roles = roles;
    }

    public UserDtoOutput(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
