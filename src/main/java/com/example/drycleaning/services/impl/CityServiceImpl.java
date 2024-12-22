package com.example.drycleaning.services.impl;

import com.example.drycleaning.dtos.input.BranchDtoInput;
import com.example.drycleaning.dtos.input.CityDtoInput;
import com.example.drycleaning.dtos.output.BranchDtoOutput;
import com.example.drycleaning.dtos.output.CityDtoOutput;
import com.example.drycleaning.models.City;
import com.example.drycleaning.repositories.CityRepository;
import com.example.drycleaning.repositories.OrderRepository;
import com.example.drycleaning.services.CityService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableCaching
public class CityServiceImpl implements CityService {

    private CityRepository cityRepository;

    private ModelMapper modelMapper;


    @Autowired
    public void setCityRepository(CityRepository cityRepository){
        this.cityRepository = cityRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    @Override
    public CityDtoOutput findById(String cityId) {
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new IllegalArgumentException("Город не найден"));

        return modelMapper.map(city, CityDtoOutput.class);
    }

    @Override
    public CityDtoOutput findByBranchId(String branchId) {
        String cityId =cityRepository.findIdByBranchId(branchId);
        if (cityId == null) {
            throw new IllegalArgumentException("Город не найден");
        }
        return modelMapper.map(cityRepository.findById(cityId), CityDtoOutput.class);
    }


    @Override
    @Cacheable("cities")
    public List<CityDtoOutput> findAll() {
            return cityRepository.getAll().stream().map(city -> modelMapper.map(city, CityDtoOutput.class)).toList();
    }

}
