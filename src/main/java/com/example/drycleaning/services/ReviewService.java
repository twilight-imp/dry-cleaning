package com.example.drycleaning.services;

import com.example.drycleaning.dtos.input.ReviewDtoInput;
import com.example.drycleaning.dtos.output.ReviewDtoOutput;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

public interface ReviewService {


    ReviewDtoOutput add(ReviewDtoInput reviewDtoInput);

    List<ReviewDtoOutput> findAll();

    ReviewDtoOutput findById(String reviewId);


    boolean existsByOrderId(String orderId);

    ReviewDtoOutput findByOrderId(String orderId);
}
