package com.example.drycleaning.services;

import com.example.drycleaning.dtos.edit.UserProfileDtoEdit;
import com.example.drycleaning.dtos.edit.UserRoleDtoEdit;
import com.example.drycleaning.dtos.output.UserDtoOutput;

import java.util.List;

public interface UserService {

    List<UserDtoOutput> findAll();

    UserDtoOutput findById(String userId);


    void remove(String userId);

    void modify(UserProfileDtoEdit newUserDto, String userId);

    void updateRole(UserRoleDtoEdit newUserDto, String userId);


}
