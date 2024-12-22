package com.example.drycleaning.services.impl;

import com.example.drycleaning.dtos.output.TypeProductDtoOutput;
import com.example.drycleaning.repositories.TypeProductRepository;
import com.example.drycleaning.services.TypeProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableCaching
public class TypeProductServiceImpl implements TypeProductService {

    private TypeProductRepository typeProductRepository;
    private ModelMapper modelMapper;


    @Autowired
    public void setTypeProductRepository(TypeProductRepository typeProductRepository){
        this.typeProductRepository= typeProductRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }


    @Override
    @Cacheable("typeProducts")
    public List<TypeProductDtoOutput> findAll() {
        return typeProductRepository.getAll().stream().map(typeProduct-> modelMapper.map(typeProduct, TypeProductDtoOutput.class)).toList();

    }

    @Override
    public TypeProductDtoOutput findById(String typeProductId) {
        return modelMapper.map(typeProductRepository.findById(typeProductId).
                orElseThrow(() -> new IllegalArgumentException("Тип изделия не найден")),TypeProductDtoOutput.class);
    }

}
