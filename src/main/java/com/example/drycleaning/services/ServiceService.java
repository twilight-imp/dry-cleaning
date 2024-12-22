package com.example.drycleaning.services;

import com.example.drycleaning.dtos.input.ServiceDtoInput;
import com.example.drycleaning.dtos.output.ServiceDtoOutput;

import java.util.List;

public interface ServiceService {
    ServiceDtoOutput add(ServiceDtoInput serviceDtoInput);

    List<ServiceDtoOutput> findAll();

    List<ServiceDtoOutput> findPopular();

    ServiceDtoOutput findById(String serviceId);

    void remove(String serviceId);

    void modify(ServiceDtoInput newServiceDto, String serviceId);


    List<ServiceDtoOutput> findAllForUser(String email);
}
