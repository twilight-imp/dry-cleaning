package com.example.drycleaning.models;

import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
@Table(name = "review")
public class Review extends BaseEntity {
    private String comment;

    private int score;

    private LocalDate date;
    private Order order;

    public Review(String comment, int score, Order order) {
        this.comment = comment;
        this.score = score;
        this.order = order;
        this.date = LocalDate.now();
    }

    protected Review() {
    }

    @Column(columnDefinition = "TEXT")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Column(nullable = false)
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @OneToOne()
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order orders) {
        this.order = orders;
    }

    @Column
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}