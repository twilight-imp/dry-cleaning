package com.example.drycleaning.repositories.impl;


import com.example.drycleaning.models.Order;
import com.example.drycleaning.models.OrderStatus;
import com.example.drycleaning.repositories.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepositoryImpl extends BaseRepositoryImpl<Order> implements OrderRepository {

    @PersistenceContext
    private EntityManager entityManager;

    protected OrderRepositoryImpl() {
        super(Order.class);
    }

    @Override
    public List<Order> findByUserId(String userId) {
        TypedQuery<Order> query = entityManager.createQuery(
                "select o from Order o " +
                        "join o.user u "+
                        "where u.id = :userId and o.isDeleted = false", Order.class);
        return query.setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Order> findByBranchId(String branchId) {
        TypedQuery<Order> query = entityManager.createQuery(
                "select o from Order o " +
                        "join o.branch b "+
                        "where b.id = :branchId and o.isDeleted = false", Order.class);
        return query.setParameter("branchId", branchId)
                .getResultList();
    }


    @Override
    public Boolean existsByUserIdAndStatus(String userId, OrderStatus status) {
        TypedQuery<Boolean> query = entityManager.createQuery(
                "select count(o) > 0 from Order o " +
                        "where o.user.id = :userId " +
                        "and o.orderStatus = :status and o.isDeleted = false", Boolean.class);
        return query.setParameter("userId", userId)
                .setParameter("status", status)
                .getSingleResult();
    }

}
