package com.example.drycleaning.services.impl;


import com.example.drycleaning.dtos.input.ProductDtoInput;
import com.example.drycleaning.dtos.output.ProductDtoOutput;
import com.example.drycleaning.dtos.output.ServiceDtoOutput;
import com.example.drycleaning.models.Loyalty;
import com.example.drycleaning.models.OrderStatus;
import com.example.drycleaning.models.Product;
import com.example.drycleaning.models.User;
import com.example.drycleaning.repositories.ProductRepository;
import com.example.drycleaning.repositories.TypeProductRepository;
import com.example.drycleaning.repositories.UserRepository;
import com.example.drycleaning.services.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@EnableCaching
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;

    private UserRepository userRepository;

    private ModelMapper modelMapper;

    private TypeProductRepository typeProductRepository;


    @Autowired
    public void setProductRepository(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    @Autowired
    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Autowired
    public void setTypeProductRepository(TypeProductRepository typeProductRepository) {
        this.typeProductRepository = typeProductRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }


    @Override
    @CacheEvict(cacheNames = "products", allEntries = true)
    public ProductDtoOutput add(ProductDtoInput productDtoInput) {
        Product product = modelMapper.map(productDtoInput, Product.class);
        Product savedProduct = productRepository.create(product);
        if (savedProduct == null) {
            throw new RuntimeException("Не удалось добавить вещь");
        }
        return modelMapper.map(savedProduct, ProductDtoOutput.class);
    }

    @Override
    @Cacheable("products")
    public List<ProductDtoOutput> findAll() {
        return productRepository.getAll().stream()
                .map(product -> {
                    ProductDtoOutput dto = modelMapper.map(product, ProductDtoOutput.class);
                    dto.setTypeProductId(product.getTypeProduct().getId());
                    return dto;
                })
                .sorted(Comparator.comparing(ProductDtoOutput::getTypeProductId).thenComparing(ProductDtoOutput::getName))
                .toList();
    }

    @Override
    public ProductDtoOutput findById(String productId) {
        return modelMapper.map(productRepository.findById(productId).
                orElseThrow(() -> new IllegalArgumentException("Изделие не найдено")),ProductDtoOutput.class);
    }

    @Override
    @CacheEvict(cacheNames = "products", allEntries = true)
    public void remove(String productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isEmpty()) {
            throw new EntityNotFoundException("Изделие не найдено");
        }
        Product product = optionalProduct.get();

        if (product.getOrderPosition().stream().anyMatch(orderPosition -> orderPosition.getOrder().getOrderStatus() == OrderStatus.NEW)) {
            throw new IllegalStateException("Нельзя удалить вещь, для которой создается заказ");
        }
        else {
            productRepository.delete(product);}
    }

    @Override
    @CacheEvict(cacheNames = "products", allEntries = true)
    public void modify(ProductDtoInput newProductDto, String productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setName(newProductDto.getName());
            product.setMaterial(newProductDto.getMaterial());
            product.setTypeProduct(typeProductRepository.findById(newProductDto.getTypeProductId()).get());
            product.setExtraCharge(newProductDto.getExtraCharge());
            productRepository.update(product);
        } else {
            throw new EntityNotFoundException("Вещь не найдена");
        }
    }

    @Override
    public List<ProductDtoOutput> findAllForUser(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("Пользователь не найден");
        }

        User user = optionalUser.get();
        Loyalty loyalty = user.getLoyalty();
        double discount = loyalty.getDiscount();

        List<Product> products = productRepository.getAll();

        List<ProductDtoOutput> productDtos = products.stream()
                .map(product -> {
                    int originalPrice = product.getExtraCharge();
                    int discountedPrice = (int) Math.round(originalPrice - (originalPrice * discount / 100));
                    ProductDtoOutput dto = new ProductDtoOutput(product.getName(),product.getMaterial(),discountedPrice,product.getTypeProduct().getId(),product.getId(),product.getIsDeleted());
                    return dto;
                })
                .sorted(Comparator.comparing(ProductDtoOutput::getTypeProductId).thenComparing(ProductDtoOutput::getName))
                .toList();

        return productDtos;
    }
}


