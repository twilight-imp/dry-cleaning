package com.example.drycleaning.validation;

import com.example.drycleaning.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniquePhoneValidator implements ConstraintValidator<UniquePhone, String> {

    private final UserRepository userRepository;


    public UniquePhoneValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return userRepository.findByPhone(value).isEmpty();
    }
}
