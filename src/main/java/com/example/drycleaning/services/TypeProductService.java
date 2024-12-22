package com.example.drycleaning.services;

import com.example.drycleaning.dtos.input.TypeProductDtoInput;
import com.example.drycleaning.dtos.output.TypeProductDtoOutput;

import java.util.List;

public interface TypeProductService {

    List<TypeProductDtoOutput> findAll();

    TypeProductDtoOutput findById(String typeProductId);


}
