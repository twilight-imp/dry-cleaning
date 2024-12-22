package com.example.drycleaning.dtos.output;

import com.example.drycleaning.dtos.input.BranchDtoInput;

import java.io.Serializable;
import java.util.List;

public class BranchDtoOutput implements Serializable {
    private String id;

    private String name;
    private String city;
    private String address;
    private String phone;
    private Boolean isDeleted;

    private List<ReviewDtoOutput> reviews;

    private Double rating;

    public BranchDtoOutput() {
    }

    public BranchDtoOutput(String id, String name, String city, String address, String phone, Boolean isDeleted, List<ReviewDtoOutput> reviews, Double rating) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.address = address;
        this.phone = phone;
        this.isDeleted = isDeleted;
        this.reviews = reviews;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public List<ReviewDtoOutput> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewDtoOutput> reviews) {
        this.reviews = reviews;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

//    @Override
//    public String toString() {
//        return "BranchDtoOutput{" +
//                "id='" + id + '\'' +
//                ", name='" + name + '\'' +
//                ", city='" + city + '\'' +
//                ", address='" + address + '\'' +
//                ", phone='" + phone + '\'' +
//                ", isDeleted=" + isDeleted +
//                ", reviews=" + reviews +
//                ", rating=" + rating +
//                '}';
//    }
}

