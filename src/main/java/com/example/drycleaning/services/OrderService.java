package com.example.drycleaning.services;


import com.example.drycleaning.dtos.edit.OrderDtoEdit;
import com.example.drycleaning.dtos.input.OrderDtoInput;
import com.example.drycleaning.dtos.output.OrderDtoOutput;


import java.util.List;

public interface OrderService {

    OrderDtoOutput add(OrderDtoInput orderPositionDtoInput);

    List<OrderDtoOutput> findAll();

    OrderDtoOutput findById(String orderId);

    void remove(String orderId);

    void modify(OrderDtoEdit orderDtoEdit, String orderId);

    void changeBranch(String branchId, String orderId);

    List<OrderDtoOutput> findByUser(String email);

    public OrderDtoOutput findBasketUser(String email);

    void confirm(String orderId);


}
