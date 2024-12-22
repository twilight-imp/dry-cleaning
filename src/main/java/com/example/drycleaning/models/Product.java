package com.example.drycleaning.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "product")
public class Product extends BaseEntity {
    private String name;

    private String material;

    private Integer extraCharge;

    private TypeProduct typeProduct;

    private Set<OrderPosition> orderPosition;

    public Product(String name, String material, Integer extraCharge, TypeProduct typeProducts) {
        this.name = name;
        this.material = material;
        this.extraCharge = extraCharge;
        this.typeProduct = typeProducts;
    }

    protected Product() {
    }

    @Column(length = 50, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 50, nullable = false)
    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
    @Column()
    public Integer getExtraCharge() {
        return extraCharge;
    }

    public void setExtraCharge(Integer extraCharge) {
        this.extraCharge = extraCharge;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "type_products_id", referencedColumnName = "id")
    public TypeProduct getTypeProduct() {
        return typeProduct;
    }

    public void setTypeProduct(TypeProduct typeProduct) {
        this.typeProduct = typeProduct;
    }

    @OneToMany(mappedBy = "product", targetEntity = OrderPosition.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<OrderPosition> getOrderPosition() {
        return orderPosition;
    }

    public void setOrderPosition(Set<OrderPosition> orderPosition) {
        this.orderPosition = orderPosition;
    }



}


