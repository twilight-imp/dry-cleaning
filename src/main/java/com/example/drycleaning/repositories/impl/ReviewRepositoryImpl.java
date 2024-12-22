package com.example.drycleaning.repositories.impl;


import com.example.drycleaning.models.Review;
import com.example.drycleaning.repositories.ReviewRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReviewRepositoryImpl extends BaseRepositoryImpl<Review> implements ReviewRepository {

    @PersistenceContext
    private EntityManager entityManager;

    protected ReviewRepositoryImpl() {
        super(Review.class);
    }


    @Override
    public List<Review> findByBranchId(String branchId) {
        TypedQuery<Review> query = entityManager.createQuery(
                "select r from Review r " +
                        "join r.order o "+
                        "join o.branch b "+
                        "where b.id = :branchId and r.isDeleted = false", Review.class);
        return query.setParameter("branchId", branchId)
                .getResultList();
    }


    @Override
    public boolean existsByOrderId(String orderId) {
        TypedQuery<Long> query = entityManager.createQuery(
                "select count(r) from Review r " +
                        "join r.order o " +
                        "where o.id = :orderId", Long.class);
        return query.setParameter("orderId", orderId).getSingleResult() > 0;
    }

    @Override
    public Optional<Review> findReviewByOrderId(String orderId) {
        TypedQuery<Review> query = entityManager.createQuery(
                "select r from Review r " +
                        "join r.order o " +
                        "where o.id = :orderId", Review.class);
        List<Review> result = query.setParameter("orderId", orderId).getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

}
