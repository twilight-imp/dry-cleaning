package com.example.drycleaning.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "type_product")
public class TypeProduct extends BaseEntity {
    private String name;

    private Set<Product> product;


    public TypeProduct(String name) {
        this.name = name;
    }

    protected TypeProduct() {
    }

    @Column(length = 50, nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "typeProduct", targetEntity = Product.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<Product> getProduct() {
        return product;
    }

    public void setProduct(Set<Product> product) {
        this.product = product;
    }

}
