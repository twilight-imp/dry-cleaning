package com.example.drycleaning.dtos.input;

import java.io.Serializable;

public class ReviewDtoInput implements Serializable {

    private String comment;

    private int score;

    private String orderId;

    public ReviewDtoInput(){}

    public ReviewDtoInput(String comment, int score, String orderId) {
        this.comment = comment;
        this.score = score;
        this.orderId = orderId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
