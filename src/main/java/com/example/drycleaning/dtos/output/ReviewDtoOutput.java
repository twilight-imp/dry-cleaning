package com.example.drycleaning.dtos.output;

import com.example.drycleaning.dtos.input.ReviewDtoInput;

import java.io.Serializable;
import java.time.LocalDate;

public class ReviewDtoOutput extends ReviewDtoInput implements Serializable {

    private String id;

    private LocalDate date;

    private String userName;

    private Boolean isDeleted;

    public ReviewDtoOutput(){}

    public ReviewDtoOutput(String comment, int score, String orderId) {
        super(comment, score, orderId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
