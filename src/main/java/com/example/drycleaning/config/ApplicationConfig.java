package com.example.drycleaning.config;

import com.example.drycleaning.dtos.edit.UserProfileDtoEdit;
import com.example.drycleaning.models.*;
import com.example.drycleaning.dtos.input.*;
import com.example.drycleaning.dtos.output.*;
import com.example.drycleaning.repositories.*;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

@Bean
public ModelMapper modelMapper(ProductRepository productRepository,
                               ServiceRepository serviceRepository,
                               OrderRepository orderRepository,
                               CityRepository cityRepository,
                               TypeProductRepository typeProductRepository,
                               BranchRepository branchRepository
) {

    ModelMapper modelMapper = new ModelMapper();
    Converter<Product, String> toProductId = ctx -> ctx.getSource() == null ? null: ctx.getSource().getId();
    Converter<Service, String> toServiceId = ctx -> ctx.getSource() == null ? null: ctx.getSource().getId();
    Converter<Order, String> toOrderId = ctx -> ctx.getSource() == null ? null: ctx.getSource().getId();
    Converter<City, String> toCityId = ctx -> ctx.getSource() == null ? null: ctx.getSource().getId();
    Converter<TypeProduct, String> toTypeProductId = ctx -> ctx.getSource() == null ? null: ctx.getSource().getId();
    Converter<Branch, String> toBranchId = ctx -> ctx.getSource() == null ? null: ctx.getSource().getId();

    Converter<String, Product> toProductEntity = ctx -> productRepository.findById(ctx.getSource()).orElse(null);
    Converter<String, Service> toServiceEntity = ctx -> serviceRepository.findById(ctx.getSource()).orElse(null);
    Converter<String, Order> toOrderEntity = ctx -> orderRepository.findById(ctx.getSource()).orElse(null);
    Converter<String, City> toCityEntity = ctx -> cityRepository.findById(ctx.getSource()).orElse(null);
    Converter<String,TypeProduct> toTypeProductEntity = ctx -> typeProductRepository.findById(ctx.getSource()).orElse(null);
    Converter<String,Branch> toBranchEntity = ctx -> branchRepository.findById(ctx.getSource()).orElse(null);

    modelMapper.addMappings(new PropertyMap<OrderPositionDtoInput, OrderPosition>() {
        @Override
        protected void configure() {
            using(toProductEntity).map(source.getProduct(), destination.getProduct());
            using(toServiceEntity).map(source.getService(), destination.getService());
            using(toOrderEntity).map(source.getOrder(), destination.getOrder());
        }
    });

    modelMapper.addMappings(new PropertyMap<OrderPosition, OrderPositionDtoOutput>() {
        @Override
        protected void configure() {
            using(toProductId).map(source.getProduct(), destination.getProduct());
            using(toServiceId).map(source.getService(), destination.getService());
            using(toOrderId).map(source.getOrder(), destination.getOrder());
        }
    });

    modelMapper.addMappings(new PropertyMap<UserProfileDtoEdit, User>() {
        @Override
        protected void configure() {
            using(toCityEntity).map(source.getCity(), destination.getCity());
        }
    });

    modelMapper.addMappings(new PropertyMap<User, UserDtoOutput>() {
        @Override
        protected void configure() {
            using(toCityId).map(source.getCity(), destination.getCity());
        }
    });

    modelMapper.addMappings(new PropertyMap<ReviewDtoInput, User>() {
        @Override
        protected void configure() {
            using(toOrderEntity).map(source.getOrderId(), destination.getOrder());
        }
    });

    modelMapper.addMappings(new PropertyMap<User, ReviewDtoOutput>() {
        @Override
        protected void configure() {
            using(toOrderId).map(source.getOrder(), destination.getOrderId());
        }
    });

    modelMapper.addMappings(new PropertyMap<ProductDtoInput, Product>() {
        @Override
        protected void configure() {
            using(toTypeProductEntity).map(source.getTypeProductId(), destination.getTypeProduct());
        }
    });

    modelMapper.addMappings(new PropertyMap<Product, ProductDtoOutput>() {
        @Override
        protected void configure() {
            using(toTypeProductId).map(source.getTypeProduct(), destination.getTypeProductId());
        }
    });

    modelMapper.addMappings(new PropertyMap<BranchDtoInput, Branch>() {
        @Override
        protected void configure() {
            using(toCityEntity).map(source.getCity(), destination.getCity());
        }
    });

    modelMapper.addMappings(new PropertyMap<Branch, BranchDtoOutput>() {
        @Override
        protected void configure() {
            using(toCityId).map(source.getCity(), destination.getCity());
        }
    });


    modelMapper.addMappings(new PropertyMap<Order, OrderDtoOutput>() {
        @Override
        protected void configure() {
            using(toBranchId).map(source.getBranch(), destination.getBranchId());
        }
    });

    return modelMapper;
}
}