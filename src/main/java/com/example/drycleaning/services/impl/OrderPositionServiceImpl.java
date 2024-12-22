package com.example.drycleaning.services.impl;


import com.example.drycleaning.dtos.input.OrderPositionDtoInput;
import com.example.drycleaning.dtos.output.OrderPositionDtoOutput;
import com.example.drycleaning.models.Order;
import com.example.drycleaning.models.OrderPosition;
import com.example.drycleaning.models.OrderStatus;
import com.example.drycleaning.models.User;
import com.example.drycleaning.repositories.OrderPositionRepository;
import com.example.drycleaning.repositories.OrderRepository;
import com.example.drycleaning.repositories.ProductRepository;
import com.example.drycleaning.repositories.ServiceRepository;
import com.example.drycleaning.services.OrderPositionService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@EnableCaching
public class OrderPositionServiceImpl implements OrderPositionService {
    private OrderPositionRepository orderPositionRepository;

    private OrderRepository orderRepository;
    private ProductRepository productRepository;

    private ServiceRepository serviceRepository;

    private ModelMapper modelMapper;

    @Autowired
    public void setProductRepository(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    @Autowired
    public void setServiceRepository(ServiceRepository serviceRepository){
        this.serviceRepository = serviceRepository;
    }
    @Autowired
    public void setOrderRepository(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }



    @Autowired
    public void setOrderPositionRepository(OrderPositionRepository orderPositionRepository){
        this.orderPositionRepository = orderPositionRepository;
    }
    @Autowired
    public void setModelMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }


    @Override
    @CacheEvict(cacheNames = "positions", allEntries = true)
    public OrderPositionDtoOutput add(OrderPositionDtoInput orderPositionDtoInput) {
        OrderPosition orderPosition = modelMapper.map(orderPositionDtoInput, OrderPosition.class);
        Order order = orderPosition.getOrder();
        User user = order.getUser();

        int productPrice = productRepository.findById(orderPositionDtoInput.getProduct()).get().getExtraCharge();
        int servicePrice = serviceRepository.findById(orderPositionDtoInput.getService()).get().getCost();

        int discountedProductPrice = (int) Math.ceil(productPrice - (productPrice * user.getLoyalty().getDiscount()/ 100));
        int discountedServicePrice = (int) Math.ceil(servicePrice - (servicePrice * user.getLoyalty().getDiscount() / 100));

        int amount = (discountedProductPrice + discountedServicePrice) * orderPosition.getNumber();

        Optional<OrderPosition> oldPosition = orderPositionRepository.findByOrderAndProductAndService(
                order.getId(),
                orderPosition.getProduct().getId(),
                orderPosition.getService().getId()
        );
        if (oldPosition.isPresent()) {
            OrderPosition old = oldPosition.get();
            int newNumber = old.getNumber() + orderPosition.getNumber();
            int newAmount = old.getAmount() + amount;

            old.setNumber(newNumber);
            old.setAmount(newAmount);

            order.setFinalPrice(order.getFinalPrice() + amount);
            orderRepository.update(order);
            orderPositionRepository.update(old);
            return modelMapper.map(old, OrderPositionDtoOutput.class);
        }

        orderPosition.setAmount(amount);
        OrderPosition savedOrderPosition = orderPositionRepository.create(orderPosition);

        order.setFinalPrice(order.getFinalPrice() + amount);
        orderRepository.update(order);

        if (savedOrderPosition == null) {
            throw new RuntimeException("Не удалось создать позицию заказа");
        }


        return modelMapper.map(savedOrderPosition, OrderPositionDtoOutput.class);
    }


    @Override
    @Cacheable("positions")
    public List<OrderPositionDtoOutput> findAll() {
        return orderPositionRepository.getAll().stream().map(orderPosition-> modelMapper.map(orderPosition, OrderPositionDtoOutput.class)).toList();
    }

    @Override
    public OrderPositionDtoOutput findById(String orderPositionId) {
        return modelMapper.map(orderPositionRepository.findById(orderPositionId).
                orElseThrow(() -> new IllegalArgumentException("Позиция заказа не найдена")),OrderPositionDtoOutput.class);
    }

    @Override
    public void remove(String orderPositionId) {
        Optional<OrderPosition> optionalOrderPosition = orderPositionRepository.findById(orderPositionId);
        if (optionalOrderPosition.isEmpty()) {
            throw new EntityNotFoundException("Позиция заказа не найдена");
        }
        OrderPosition orderPosition = optionalOrderPosition.get();


        Order order = orderPosition.getOrder();

        if (!order.getOrderStatus().equals(OrderStatus.NEW)) {
            throw new IllegalStateException("Заказ формируется. Удаление невозможно");
        }
        order.setFinalPrice(order.getFinalPrice() - orderPosition.getAmount());
        orderRepository.update(order);
        orderPositionRepository.delete(optionalOrderPosition.get());
    }

    @Override
    @CacheEvict(cacheNames = "positions", allEntries = true)
    public void modify(OrderPositionDtoInput newOrderPositionDto, String orderPositionId) {
        Optional<OrderPosition> optionalOrderPosition = orderPositionRepository.findById(orderPositionId);

        if (optionalOrderPosition.isPresent()) {
            OrderPosition orderPosition = optionalOrderPosition.get();
            Order order = orderPosition.getOrder();
            User user = orderPosition.getOrder().getUser();
            int oldAmount = orderPosition.getAmount();

            int productPrice = productRepository.findById(newOrderPositionDto.getProduct()).get().getExtraCharge();
            int servicePrice = serviceRepository.findById(newOrderPositionDto.getService()).get().getCost();

            int discountedProductPrice = (int) Math.ceil(productPrice - (productPrice * user.getLoyalty().getDiscount()/ 100));
            int discountedServicePrice = (int) Math.ceil(servicePrice - (servicePrice * user.getLoyalty().getDiscount() / 100));
            int newAmount = (discountedProductPrice + discountedServicePrice) * orderPosition.getNumber();

            orderPosition.setNumber(newOrderPositionDto.getNumber());
            orderPosition.setAmount(newAmount);
            orderPosition.setProduct(productRepository.findById(newOrderPositionDto.getProduct()).get());
            orderPosition.setService(serviceRepository.findById(newOrderPositionDto.getService()).get());
            order.setFinalPrice(order.getFinalPrice() - oldAmount + newAmount);
            orderPositionRepository.update(orderPosition);
            orderRepository.update(order);
        } else {
            throw new EntityNotFoundException("Позиция заказа не найдена");
        }
    }

    @Override
    public List<OrderPositionDtoOutput> findByOrder(String orderId) {
        List<OrderPosition> orderPositions = orderPositionRepository.findByOrderId(orderId);

        if (orderPositions == null || orderPositions.isEmpty()) {
            throw new EntityNotFoundException("Позиции заказа не найдены для заказа");
        }
        return orderPositions.stream().map(orderPosition -> modelMapper.map(orderPosition, OrderPositionDtoOutput.class)).toList();
    }



}


