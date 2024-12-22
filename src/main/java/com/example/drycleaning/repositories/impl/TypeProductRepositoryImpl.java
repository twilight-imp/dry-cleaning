package com.example.drycleaning.repositories.impl;

import com.example.drycleaning.models.TypeProduct;
import com.example.drycleaning.repositories.TypeProductRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TypeProductRepositoryImpl extends BaseRepositoryImpl<TypeProduct> implements TypeProductRepository {


    protected TypeProductRepositoryImpl() {
        super(TypeProduct.class);
    }
}
