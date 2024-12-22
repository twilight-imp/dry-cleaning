package com.example.drycleaning.repositories.impl;


import com.example.drycleaning.models.Product;
import com.example.drycleaning.repositories.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepositoryImpl extends BaseRepositoryImpl<Product> implements ProductRepository   {
    @PersistenceContext
    private EntityManager entityManager;

    protected ProductRepositoryImpl() {
        super(Product.class);
    }

}
