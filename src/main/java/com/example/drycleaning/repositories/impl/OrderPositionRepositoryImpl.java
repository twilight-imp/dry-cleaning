package com.example.drycleaning.repositories.impl;


import com.example.drycleaning.models.OrderPosition;
import com.example.drycleaning.repositories.OrderPositionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderPositionRepositoryImpl extends BaseRepositoryImpl<OrderPosition> implements OrderPositionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    protected OrderPositionRepositoryImpl() {
        super(OrderPosition.class);
    }

    @Override
    public List<OrderPosition> findByOrderId(String orderId) {
        TypedQuery<OrderPosition> query = entityManager.createQuery(
                "select p from OrderPosition p " +
                        "join p.order o "+
                        "where o.id = :orderId ", OrderPosition.class);
        return query.setParameter("orderId", orderId)
                .getResultList();
    }

    @Override
    public Optional<OrderPosition> findByOrderAndProductAndService(String orderId, String productId, String serviceId) {
        TypedQuery<OrderPosition> query = entityManager.createQuery(
                "select p from OrderPosition p " +
                        "where p.order.id = :orderId and p.product.id = :productId and p.service.id = :serviceId " +
                        "and p.isDeleted = false",
                OrderPosition.class);
        List<OrderPosition> result = query
                .setParameter("orderId", orderId)
                .setParameter("productId", productId)
                .setParameter("serviceId", serviceId)
                .getResultList();
        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(result.get(0));
        }
    }
}
