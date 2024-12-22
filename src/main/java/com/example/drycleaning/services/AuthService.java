package com.example.drycleaning.services;

import com.example.drycleaning.dtos.input.UserRegistrationDto;
import com.example.drycleaning.models.User;
import jakarta.validation.Valid;

public interface AuthService {

    void register(@Valid UserRegistrationDto registrationDTO);

    User getUser(String email);
}
