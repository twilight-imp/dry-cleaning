package com.example.drycleaning.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "branch")
public class Branch extends BaseEntity {
    private String name;
    private City city;

    private String address;
    private String phone;

    private Set<Order> order;

    public Branch(String name, City city, String address, String phone) {
        this.name = name;
        this.city = city;
        this.address = address;
        this.phone = phone;
    }

    protected Branch() {
    }

    @Column(length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Column(length = 11)
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }


    @OneToMany(mappedBy = "branch", targetEntity = Order.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    public Set<Order> getOrder() {
        return order;
    }

    public void setOrder(Set<Order> order) {
        this.order = order;
    }

    @Column(columnDefinition = "TEXT")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}

