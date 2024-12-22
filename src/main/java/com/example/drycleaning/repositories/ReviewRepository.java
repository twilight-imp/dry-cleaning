package com.example.drycleaning.repositories;

import com.example.drycleaning.models.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends BaseRepository<Review> {

    List<Review> findByBranchId(String branchId);

    boolean existsByOrderId(String orderId);

  Optional<Review> findReviewByOrderId(String orderId);

}
