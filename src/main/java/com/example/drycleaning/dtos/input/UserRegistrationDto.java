package com.example.drycleaning.dtos.input;


import com.example.drycleaning.validation.UniqueEmail;
import com.example.drycleaning.validation.UniquePhone;
import jakarta.validation.constraints.*;

import java.io.Serializable;

public class UserRegistrationDto implements Serializable {

    private String lastName;
    private String name;
    @UniquePhone
    private String phone;
    @UniqueEmail
    private String email;

    private String password;
    private String confirmPassword;
    private String city;

    public UserRegistrationDto(String lastName, String name, String phone, String email, String password, String confirmPassword, String city) {
        this.lastName = lastName;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.city = city;
    }

    public UserRegistrationDto() {}

    @NotEmpty(message = "User lastname cannot be null or empty!")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @NotEmpty(message = "User name cannot be null or empty!")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotEmpty(message = "User phone cannot be null or empty!")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @NotEmpty(message = "User city cannot be null or empty!")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @NotEmpty(message = "Email cannot be null or empty!")
    @Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @NotEmpty(message = "Password cannot be null or empty!")
    @Size(min = 5, max = 20)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @NotEmpty(message = "Confirm Password cannot be null or empty!")
    @Size(min = 5, max = 20)
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Override
    public String toString() {
        return "UserRegistrationDto{" +
                "lastName='" + lastName + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}

