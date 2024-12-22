package com.example.drycleaning.services.impl;

import com.example.drycleaning.dtos.edit.UserProfileDtoEdit;
import com.example.drycleaning.dtos.edit.UserRoleDtoEdit;
import com.example.drycleaning.dtos.output.UserDtoOutput;
import com.example.drycleaning.errors.AddUserException;
import com.example.drycleaning.models.*;
import com.example.drycleaning.repositories.LoyaltyRepository;
import com.example.drycleaning.repositories.OrderRepository;
import com.example.drycleaning.repositories.UserRepository;
import com.example.drycleaning.repositories.UserRoleRepository;
import com.example.drycleaning.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@EnableCaching
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    private OrderRepository orderRepository;

    private UserRoleRepository userRoleRepository;

    private LoyaltyRepository loyaltyRepository;

    private PasswordEncoder passwordEncoder;
    private ModelMapper modelMapper;


    @Autowired
    public void setLoyaltyRepository(LoyaltyRepository loyaltyRepository){
        this.loyaltyRepository = loyaltyRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Autowired
    public void setOrderRepository(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
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
    public void setModelMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }



    @Override
    @Cacheable("users")
    public List<UserDtoOutput> findAll() {
        return userRepository.getAll().stream().map(user-> modelMapper.map(user, UserDtoOutput.class)).sorted(Comparator.comparing(UserDtoOutput::getLastName)).toList();
    }

    @Override
    public UserDtoOutput findById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        UserDtoOutput dto = modelMapper.map(user, UserDtoOutput.class);
        dto.setRoles(user.getRoles());
        return dto;
    }


    @Override
    @CacheEvict(cacheNames = "users", allEntries = true)
    public void remove(String userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("Пользователь не найден");
        }
        Loyalty loyalty = loyaltyRepository.findLoyaltyByUserId(userId);
        loyaltyRepository.delete(loyalty);
        List<Order> orders = orderRepository.findByUserId(userId);
        for (Order order:orders) {
            orderRepository.delete(order);
        }
        userRepository.delete(optionalUser.get());
    }

    @Override
    @CacheEvict(cacheNames = "users", allEntries = true)
    public void modify(UserProfileDtoEdit newUserDto, String userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            System.out.println(newUserDto.getPassword());
            if(!newUserDto.getPassword().isEmpty() && !passwordEncoder.matches(newUserDto.getPassword(), user.getPassword())) throw new AddUserException("Неверный пароль");

           if(!newUserDto.getEmail().equals(user.getEmail())) {
               Optional<User> byEmail = this.userRepository.findByEmail(newUserDto.getEmail());
               if (byEmail.isPresent()) {
                   throw new AddUserException("Пользователь с таким email уже зарегистрирован");
               }
           }
            if(!newUserDto.getPhone().equals(user.getPhone())) {
                Optional<User> byPhone= this.userRepository.findByPhone(newUserDto.getPhone());

                if (byPhone.isPresent()) {
                    throw new AddUserException("Пользователь с таким номером телефона уже зарегистрирован");
                }
            }

            user.setName(newUserDto.getName());
            user.setLastName(newUserDto.getLastName());
            user.setEmail(newUserDto.getEmail());
            user.setPhone(newUserDto.getPhone());
            user.setPassword(passwordEncoder.encode(newUserDto.getNewPassword()));
            userRepository.update(user);
        } else {
            throw new EntityNotFoundException("Пользователь не найден");
        }
    }

    @Transactional
    @Override
    @CacheEvict(cacheNames = "users", allEntries = true)
    public void updateRole(UserRoleDtoEdit newUserDto, String userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();

            List<Role> newRoles = newUserDto.getRole().stream()
                    .map(role -> {
                        Role existingRole = userRoleRepository.findRoleByName(role.getName()).get();
                        if (existingRole == null) {
                            existingRole = userRoleRepository.create(role);
                        }
                        return existingRole;
                    }).toList();

            user.setRoles(newRoles);
        } else {
            throw new EntityNotFoundException("Пользователь не найден");
        }
    }





}
