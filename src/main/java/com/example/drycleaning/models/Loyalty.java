package com.example.drycleaning.models;

import jakarta.persistence.*;

@Entity
@Table(name = "loyalty")
public class Loyalty extends BaseEntity {

    private User user;

    private int discount;

    private int numOrders;

    public Loyalty(User user) {
        this.user = user;
        this.discount = 0;
        this.numOrders = 0;
    }

    protected Loyalty() {
    }

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(nullable = false)
    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    @Column(nullable = false)
    public int getNumOrders() {
        return numOrders;
    }

    public void setNumOrders(int numOrders) {
        this.numOrders = numOrders;
    }



}
