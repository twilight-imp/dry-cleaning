package com.example.drycleaning.repositories;

import com.example.drycleaning.models.User;

import java.util.Optional;


public interface UserRepository extends BaseRepository<User> {

    String findNameByReview (String reviewId);

    Optional<User> findByPhone (String phone);

    Optional<User> findByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
}
