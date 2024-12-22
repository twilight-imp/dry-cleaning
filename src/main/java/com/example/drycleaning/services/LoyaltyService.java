package com.example.drycleaning.services;

import com.example.drycleaning.dtos.output.BranchDtoOutput;
import com.example.drycleaning.dtos.output.LoyaltyDtoOutput;

public interface LoyaltyService {

    LoyaltyDtoOutput findByUser(String userId);
}
