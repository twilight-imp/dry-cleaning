package com.example.drycleaning.services.impl;

import com.example.drycleaning.dtos.input.UserRegistrationDto;
import com.example.drycleaning.errors.AddUserException;
import com.example.drycleaning.models.Loyalty;
import com.example.drycleaning.models.User;
import com.example.drycleaning.models.UserRoles;
import com.example.drycleaning.repositories.CityRepository;
import com.example.drycleaning.repositories.LoyaltyRepository;
import com.example.drycleaning.repositories.UserRepository;
import com.example.drycleaning.repositories.UserRoleRepository;
import com.example.drycleaning.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;

    private LoyaltyRepository loyaltyRepository;

    private UserRoleRepository userRoleRepository;


    private PasswordEncoder passwordEncoder;

    private CityRepository cityRepository;


    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setLoyaltyRepository(LoyaltyRepository loyaltyRepository) {
        this.loyaltyRepository = loyaltyRepository;
    }

    @Autowired
    public void setUserRoleRepository(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setCityRepository(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }


    public void register(@Valid UserRegistrationDto registrationDTO) {
        if (!registrationDTO.getPassword().equals(registrationDTO.getConfirmPassword())) {
            throw new AddUserException("Пароли не совпадают");
        }
        Optional<User> byEmail = this.userRepository.findByEmail(registrationDTO.getEmail());

        if (byEmail.isPresent()) {
            throw new AddUserException("Пользователь с таким email уже зарегистрирован");
        }

        Optional<User> byPhone= this.userRepository.findByPhone(registrationDTO.getPhone());

        if (byPhone.isPresent()) {
            throw new AddUserException("Пользователь с таким номером телефона уже зарегистрирован");
        }

        var userRole = userRoleRepository.findRoleByName(UserRoles.USER).orElseThrow();

        User user = new User(
                registrationDTO.getLastName(),
                registrationDTO.getName(),
                registrationDTO.getPhone(),
                registrationDTO.getEmail(),
                passwordEncoder.encode(registrationDTO.getPassword()),
                cityRepository.findById(registrationDTO.getCity()).get()
        );

        user.setRoles(List.of(userRole));

        this.userRepository.create(user);
        this.loyaltyRepository.create(new Loyalty(user));
    }

    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + " was not found!"));
    }
}

