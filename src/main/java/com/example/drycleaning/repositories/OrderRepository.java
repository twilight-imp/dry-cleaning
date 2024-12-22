package com.example.drycleaning.repositories;

import com.example.drycleaning.models.Order;
import com.example.drycleaning.models.OrderStatus;

import java.util.List;

public interface OrderRepository extends BaseRepository<Order> {

    List<Order> findByUserId( String userId);

    List<Order> findByBranchId( String branchId);

    Boolean existsByUserIdAndStatus(String userId, OrderStatus status);




}
