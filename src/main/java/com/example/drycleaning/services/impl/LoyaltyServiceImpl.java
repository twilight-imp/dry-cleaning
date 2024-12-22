package com.example.drycleaning.services.impl;

import com.example.drycleaning.dtos.output.LoyaltyDtoOutput;
import com.example.drycleaning.dtos.output.OrderDtoOutput;
import com.example.drycleaning.models.Loyalty;
import com.example.drycleaning.models.User;
import com.example.drycleaning.repositories.LoyaltyRepository;
import com.example.drycleaning.repositories.UserRepository;
import com.example.drycleaning.services.LoyaltyService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoyaltyServiceImpl implements LoyaltyService {
    private UserRepository userRepository;

    private LoyaltyRepository loyaltyRepository;

    private ModelMapper modelMapper;


    @Autowired
    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Autowired
    public void setLoyaltyRepository(LoyaltyRepository loyaltyRepository){
        this.loyaltyRepository = loyaltyRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    @Override
    public LoyaltyDtoOutput findByUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        Loyalty loyalty = loyaltyRepository.findLoyaltyByUserId(userId);
        return modelMapper.map(loyalty, LoyaltyDtoOutput.class);
    }
}
