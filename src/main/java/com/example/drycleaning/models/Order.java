package com.example.drycleaning.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "\"order\"")
public class Order extends BaseEntity {
    private User user;
    private Branch branch;

    private LocalDate date;

    private OrderStatus orderStatus;

    private Integer finalPrice;

    private Review review;

    private Set<OrderPosition> orderPosition = new HashSet<>();



    public Order(User user, Branch branch) {
        this.user = user;
        this.branch = branch;
        this.date = LocalDate.now();
        this.orderStatus = OrderStatus.NEW;
        this.finalPrice = 0;
    }

    protected Order() {
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    @Column()
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
    @Column(name = "final_price",nullable = false)
    public Integer getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Integer finalPrice) {
        this.finalPrice = finalPrice;
    }

    @OneToOne(mappedBy = "order", targetEntity = Review.class)
    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    @OneToMany(mappedBy = "order", targetEntity = OrderPosition.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Set<OrderPosition> getOrderPosition() {
        return orderPosition;
    }

    public void setOrderPosition(Set<OrderPosition> orderPosition) {
        this.orderPosition = orderPosition;
    }


}
