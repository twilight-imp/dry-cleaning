package com.example.drycleaning.repositories.impl;

import com.example.drycleaning.models.Loyalty;
import com.example.drycleaning.repositories.LoyaltyRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class LoyaltyRepositoryImpl extends BaseRepositoryImpl<Loyalty> implements LoyaltyRepository {
    @PersistenceContext
    private EntityManager entityManager;

    protected LoyaltyRepositoryImpl() {
        super(Loyalty.class);
    }


    @Override
    public Loyalty findLoyaltyByUserId(String userId) {
        TypedQuery<Loyalty> query = entityManager.createQuery(
                "select l from Loyalty l " +
                        "join l.user u " +
                        "where u.id = :userId ", Loyalty.class);
        return query.setParameter("userId", userId)
                .getSingleResult();
    }
}
