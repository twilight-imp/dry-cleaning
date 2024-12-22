package com.example.drycleaning.services;

import com.example.drycleaning.dtos.input.CityDtoInput;
import com.example.drycleaning.dtos.output.CityDtoOutput;

import java.util.List;

public interface CityService {

    CityDtoOutput findById(String cityId);

    CityDtoOutput findByBranchId(String branchId);


    List<CityDtoOutput> findAll();

}
