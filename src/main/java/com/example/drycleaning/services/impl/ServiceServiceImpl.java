package com.example.drycleaning.services.impl;


import com.example.drycleaning.dtos.input.ServiceDtoInput;
import com.example.drycleaning.dtos.output.ServiceDtoOutput;
import com.example.drycleaning.models.Loyalty;
import com.example.drycleaning.models.OrderStatus;
import com.example.drycleaning.models.Service;
import com.example.drycleaning.models.User;
import com.example.drycleaning.repositories.ServiceRepository;
import com.example.drycleaning.repositories.UserRepository;
import com.example.drycleaning.services.ServiceService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
@EnableCaching
public class ServiceServiceImpl implements ServiceService {
    private ServiceRepository serviceRepository;

    private UserRepository userRepository;

    private ModelMapper modelMapper;


    @Autowired
    public void setServiceRepository(ServiceRepository serviceRepository){
        this.serviceRepository = serviceRepository;
    }
    @Autowired
    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }


    @Override
    @CacheEvict(cacheNames = {"services"}, allEntries = true)
    public ServiceDtoOutput add(ServiceDtoInput serviceDtoInput) {
        Service service = modelMapper.map(serviceDtoInput, Service.class);
        Service savedService = serviceRepository.create(service);
        if (savedService == null) {
            throw new RuntimeException("Не удалось добавить услугу");
        }
        return modelMapper.map(savedService, ServiceDtoOutput.class);
    }

    @Override
    @Cacheable("services")
    public List<ServiceDtoOutput> findAll() {
        return serviceRepository.getAll().stream()
                .map(service-> modelMapper.map(service, ServiceDtoOutput.class))
                .sorted(Comparator.comparing(ServiceDtoOutput::getName))
                .toList();
    }

    @Override
    public List<ServiceDtoOutput> findPopular() {
        return serviceRepository.findPopularFromDate(LocalDate.now().minusMonths(3)).stream().map(service-> modelMapper.map(service, ServiceDtoOutput.class)).limit(5).toList();
    }

    @Override
    public ServiceDtoOutput findById(String serviceId) {
        return modelMapper.map(serviceRepository.findById(serviceId).
                orElseThrow(() -> new IllegalArgumentException("Услуга не найдена")),ServiceDtoOutput.class);
    }

    @Override
    @CacheEvict(cacheNames = {"services"}, allEntries = true)
    public void remove(String serviceId) {
        Optional<Service> optionalService = serviceRepository.findById(serviceId);

        if (optionalService.isEmpty()) {
            throw new EntityNotFoundException("Услуга не найдена");
        }

        Service service = optionalService.get();

        if (service.getIsDeleted()) {
            throw new IllegalStateException("Услуга уже удалена");
        }

        if (service.getOrderPositions().stream().anyMatch(orderPosition -> orderPosition.getOrder().getOrderStatus() == OrderStatus.NEW)) {
            throw new IllegalStateException("Нельзя удалить услугу, для которой создается заказ");
        }
        else {
            serviceRepository.delete(service);
        }
    }

    @Override
    @CacheEvict(cacheNames = {"services"}, allEntries = true)
    public void modify(ServiceDtoInput newServiceDto, String serviceId) {
        Optional<Service> optionalService = serviceRepository.findById(serviceId);
        if (optionalService.isPresent()) {
            Service service = optionalService.get();
            service.setName(newServiceDto.getName());
            service.setDescription(newServiceDto.getDescription());
            service.setCost(newServiceDto.getCost());
            serviceRepository.update(service);
        } else {
            throw new EntityNotFoundException("Услуга не найдена");
        }
    }

    @Override
    public List<ServiceDtoOutput> findAllForUser(String  email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("Пользователь не найден");
        }

        User user = optionalUser.get();
        Loyalty loyalty = user.getLoyalty();
        double discount = loyalty.getDiscount();

        List<Service> services = serviceRepository.getAll();

        List<ServiceDtoOutput> serviceDtos = services.stream()
                .map(service -> {
                    int originalPrice = service.getCost();
                    int discountedPrice = (int) Math.round(originalPrice - (originalPrice * discount / 100));
                    ServiceDtoOutput dto = new ServiceDtoOutput(service.getId(),service.getName(),service.getDescription(),discountedPrice, service.getIsDeleted());
                    return dto;
                })
                .sorted(Comparator.comparing(ServiceDtoOutput::getName))
                .toList();

        return serviceDtos;
    }
}




