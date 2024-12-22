package com.example.drycleaning.repositories;

import com.example.drycleaning.models.OrderPosition;

import java.util.List;
import java.util.Optional;

public interface OrderPositionRepository extends BaseRepository<OrderPosition> {

    List<OrderPosition> findByOrderId(String orderId);

    Optional<OrderPosition> findByOrderAndProductAndService(String orderId, String productId, String serviceId);
}
