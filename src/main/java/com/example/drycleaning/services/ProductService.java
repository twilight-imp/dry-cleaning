package com.example.drycleaning.services;

import com.example.drycleaning.dtos.input.ProductDtoInput;
import com.example.drycleaning.dtos.output.ProductDtoOutput;

import java.util.List;

public interface ProductService {

    ProductDtoOutput add(ProductDtoInput productDtoInput);

    List<ProductDtoOutput> findAll();

    ProductDtoOutput findById(String productId);

    void remove(String productId);

    void modify(ProductDtoInput newProductDto, String productId);


    List<ProductDtoOutput> findAllForUser(String email);

}
