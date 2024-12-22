package com.example.drycleaning.services;

import com.example.drycleaning.dtos.input.OrderPositionDtoInput;
import com.example.drycleaning.dtos.output.OrderPositionDtoOutput;

import java.util.List;

public interface OrderPositionService {

    OrderPositionDtoOutput add(OrderPositionDtoInput orderPositionDtoInput);

    List<OrderPositionDtoOutput> findAll();

    OrderPositionDtoOutput findById(String orderPositionId);

    void remove(String orderPositionId);

    void modify(OrderPositionDtoInput newOrderPositionDto, String orderPositionId);


    List<OrderPositionDtoOutput> findByOrder(String orderId);

}
