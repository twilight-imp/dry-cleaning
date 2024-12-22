package com.example.drycleaning.services.impl;


import com.example.drycleaning.dtos.edit.OrderDtoEdit;
import com.example.drycleaning.dtos.input.OrderDtoInput;
import com.example.drycleaning.dtos.output.OrderDtoOutput;
import com.example.drycleaning.dtos.output.OrderPositionDtoOutput;
import com.example.drycleaning.models.*;
import com.example.drycleaning.repositories.*;
import com.example.drycleaning.services.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@EnableCaching
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    private OrderPositionRepository orderPositionRepository;

    private LoyaltyRepository loyaltyRepository;

    private UserRepository userRepository;

    private BranchRepository branchRepository;

    private ModelMapper modelMapper;

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    @Autowired
    public void setOrderPositionRepository(OrderPositionRepository orderPositionRepository){
        this.orderPositionRepository = orderPositionRepository;
    }

    @Autowired
    public void setLoyaltyRepository(LoyaltyRepository loyaltyRepository){
        this.loyaltyRepository = loyaltyRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Autowired
    public void setBranchRepository(BranchRepository branchRepository){
        this.branchRepository = branchRepository;
    }
    @Autowired
    public void setModelMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderDtoOutput add(OrderDtoInput orderDto) {
        String userId = orderDto.getUserId();
        boolean hasActiveOrder = orderRepository.existsByUserIdAndStatus(userId, OrderStatus.NEW);
        if (hasActiveOrder) {
            throw new IllegalStateException("Есть активная корзина");
        }
        Order order = modelMapper.map(orderDto, Order.class);
        return modelMapper.map(orderRepository.create(order), OrderDtoOutput.class);
    }

    @Override
    @Cacheable("orders")
    public List<OrderDtoOutput> findAll() {
        return orderRepository.getAll().stream()
                .filter(order -> !order.getOrderStatus().equals(OrderStatus.NEW))
                .sorted(Comparator
                        .comparing(Order::getDate).reversed()
                        .thenComparing(Order::getOrderStatus))
                .map(order -> modelMapper.map(order, OrderDtoOutput.class))
                .toList();
    }

    @Override
    public OrderDtoOutput findById(String orderId) {
        return modelMapper.map(orderRepository.findById(orderId).
                orElseThrow(() -> new IllegalArgumentException("Заказ не найден")),OrderDtoOutput.class);
    }

    @Override
    @CacheEvict(cacheNames = "orders", allEntries = true)
    public void remove(String orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            throw new EntityNotFoundException("Заказ не найден");
        }

        List<OrderPosition> orderPositions = orderPositionRepository.findByOrderId(orderId);
        for (OrderPosition orderPosition : orderPositions) {
            orderPositionRepository.delete(orderPosition);
        }
        orderRepository.delete(optionalOrder.get());
    }

    @Override
    @CacheEvict(cacheNames = "orders", allEntries = true)
    public void modify(OrderDtoEdit orderDtoEdit, String orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            OrderStatus orderStatus = OrderStatus.valueOf(orderDtoEdit.getOrderStatus());
            order.setOrderStatus(orderStatus);
            if (orderStatus.equals(OrderStatus.CREATED)) order.setDate(LocalDate.now());
            if (orderStatus.equals(OrderStatus.COMPLETED)){
                Loyalty loyalty = order.getUser().getLoyalty();
                loyalty.setNumOrders(loyalty.getNumOrders() + 1);
                int numOrders = loyalty.getNumOrders();
                if (numOrders == 10) {
                    loyalty.setDiscount(5);
                } else if (numOrders == 20) {
                    loyalty.setDiscount(10);
                } else if (numOrders == 30) {
                    loyalty.setDiscount(15);
                } else if (numOrders == 50) {
                    loyalty.setDiscount(25);
                }
                loyaltyRepository.update(loyalty);
            }
            order.setBranch(branchRepository.findById(orderDtoEdit.getBranchId()).get());
            orderRepository.update(order);
        } else {
            throw new EntityNotFoundException("Заказ не найден");
        }

    }

    @Override
    @CacheEvict(cacheNames = "orders", allEntries = true)
    public void changeBranch(String branchId, String orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setBranch(branchRepository.findById(branchId).get());
            orderRepository.update(order);
        } else {
            throw new EntityNotFoundException("Заказ не найден");
        }
    }


    @Override
    public List<OrderDtoOutput> findByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        List<Order> orders = orderRepository.findByUserId(user.getId());

        return orders.stream().filter(order -> !order.getOrderStatus().equals(OrderStatus.NEW))
                .map(order -> {
                    List<OrderPosition> filteredPositions = order.getOrderPosition().stream()
                            .filter(position -> !position.getIsDeleted())
                            .toList();
                    List<OrderPositionDtoOutput> positionDtos = filteredPositions.stream()
                            .map(position -> modelMapper.map(position, OrderPositionDtoOutput.class))
                            .toList();
                    OrderDtoOutput dto = modelMapper.map(order, OrderDtoOutput.class);
                    dto.setBranchId(order.getBranch().getId());
                    dto.setOrderPosition(positionDtos);
                    return dto;
                })
                .sorted(Comparator.comparing(OrderDtoOutput::getDate).reversed()
                        .thenComparing(OrderDtoOutput::getOrderStatus))

                .toList();
        }

    @Transactional
    @Override
    public OrderDtoOutput findBasketUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        List<Order> orders = orderRepository.findByUserId(user.getId());

        Order order = orders.stream()
                .filter(o -> OrderStatus.NEW.equals(o.getOrderStatus()))
                .findFirst()
                .orElse(null);

        if (order == null) {
            Order newOrder = new Order(user, branchRepository.findDefaultBranch(user.getId()));
            orderRepository.create(newOrder);
            order = newOrder;
        }
        List<OrderPosition> filteredPositions = order.getOrderPosition().stream()
                .filter(position -> !position.getIsDeleted())
                .toList();
        List<OrderPositionDtoOutput> positionDtos = filteredPositions.stream()
                .map(position -> modelMapper.map(position, OrderPositionDtoOutput.class))
                .toList();

        OrderDtoOutput dto = modelMapper.map(order, OrderDtoOutput.class);
        dto.setBranchId(order.getBranch().getId());
        dto.setOrderPosition(positionDtos);

        return dto;
    }

    @Override
    @CacheEvict(cacheNames = "orders", allEntries = true)
    public void confirm(String orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setOrderStatus(OrderStatus.CREATED);
            order.setDate(LocalDate.now());
            orderRepository.update(order);
        } else {
            throw new EntityNotFoundException("Заказ не найден");
        }
    }

}

