package com.example.drycleaning.services.impl;


import com.example.drycleaning.dtos.input.ReviewDtoInput;
import com.example.drycleaning.dtos.output.ReviewDtoOutput;
import com.example.drycleaning.models.Order;
import com.example.drycleaning.models.Review;
import com.example.drycleaning.repositories.LoyaltyRepository;
import com.example.drycleaning.repositories.OrderRepository;
import com.example.drycleaning.repositories.ReviewRepository;
import com.example.drycleaning.services.ReviewService;
import jakarta.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@EnableCaching
public class ReviewServiceImpl implements ReviewService {
    private ReviewRepository reviewRepository;

    private OrderRepository orderRepository;

    private ModelMapper modelMapper;

    @Autowired
    public void setReviewRepository(ReviewRepository reviewRepository){
        this.reviewRepository = reviewRepository;
    }

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }



    @Override
    @CacheEvict(cacheNames = {"branches"}, allEntries = true)
    public ReviewDtoOutput add(ReviewDtoInput reviewDtoInput) {
        Review review = modelMapper.map(reviewDtoInput, Review.class);
        review.setDate(LocalDate.now());
        Review savedReview = reviewRepository.create(review);
        if (savedReview == null) {
            throw new RuntimeException("Не удалось создать отзыв");
        }
        return modelMapper.map(savedReview, ReviewDtoOutput.class);
    }

    @Override
    public List<ReviewDtoOutput> findAll() {
        return reviewRepository.getAll().stream().map(review-> modelMapper.map(review, ReviewDtoOutput.class)).toList();
    }

    @Override
    public ReviewDtoOutput findById(String reviewId) {
        return modelMapper.map(reviewRepository.findById(reviewId).
                orElseThrow(() -> new IllegalArgumentException("Отзыв не найден")),ReviewDtoOutput.class);
    }

    @Override
    public boolean existsByOrderId(String orderId) {
        return reviewRepository.existsByOrderId(orderId);
    }

    @Override
    public ReviewDtoOutput findByOrderId(String orderId) {
        return reviewRepository.findReviewByOrderId(orderId)
                .map(review -> modelMapper.map(review, ReviewDtoOutput.class))
                .orElse(null);
    }

}


