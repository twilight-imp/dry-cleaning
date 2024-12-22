package com.example.drycleaning.services.impl;


import com.example.drycleaning.dtos.input.BranchDtoInput;
import com.example.drycleaning.dtos.output.BranchDtoOutput;
import com.example.drycleaning.dtos.output.ProductDtoOutput;
import com.example.drycleaning.dtos.output.ReviewDtoOutput;
import com.example.drycleaning.models.Branch;
import com.example.drycleaning.models.OrderStatus;
import com.example.drycleaning.models.Review;
import com.example.drycleaning.models.User;
import com.example.drycleaning.repositories.*;
import com.example.drycleaning.services.BranchService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@EnableCaching
public class BranchServiceImpl implements BranchService {

    private BranchRepository branchRepository;
    private ReviewRepository reviewRepository;

    private UserRepository userRepository;

    private CityRepository cityRepository;

    private ModelMapper modelMapper;

    @Autowired
    public void setBranchRepository(BranchRepository branchRepository){
        this.branchRepository = branchRepository;
    }

    @Autowired
    public void setReviewRepository(ReviewRepository reviewRepository){
        this.reviewRepository = reviewRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Autowired
    public void setCityRepository(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }


    @Override
    @CacheEvict(cacheNames = {"branches", "rating_branches"}, allEntries = true)
    public BranchDtoOutput add(BranchDtoInput branchDto) {
        Branch branch = modelMapper.map(branchDto,Branch.class);
        return modelMapper.map(branchRepository.create(branch), BranchDtoOutput.class);
    }

    @Override
    @Cacheable("branches")
    public List<BranchDtoOutput> findAll() {
        return branchRepository.getAll().stream()
                .map(branch -> {
                    return modelMapper.map(branch, BranchDtoOutput.class);
                })
                .sorted(Comparator.comparing(BranchDtoOutput::getCity)
                        .thenComparing(BranchDtoOutput::getName))
                .toList();
    }

    @Override
    public BranchDtoOutput findById(String branchId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new IllegalArgumentException("Филиал не найден"));
        BranchDtoOutput dto = modelMapper.map(branch, BranchDtoOutput.class);
        dto.setCity(branch.getCity().getId());
        return dto;
    }

    @Override
    @CacheEvict(cacheNames = {"branches", "rating_branches"}, allEntries = true)
    public void remove(String branchId) {
        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()) {
            throw new EntityNotFoundException("Филиал не найден");
        }
        Branch branch = optionalBranch.get();

        if (!branch.getOrder().isEmpty()) {
            List<Review> reviews = reviewRepository.findByBranchId(branchId);
            for (Review review : reviews) {
                reviewRepository.delete(review);
            }
            branchRepository.delete(branch);
            return;
        }
        if (branch.getOrder().stream().anyMatch(order -> order.getOrderStatus() != OrderStatus.COMPLETED && order.getOrderStatus() != OrderStatus.CANCELLED)) {
            throw new IllegalStateException("Нельзя закрыть филиал с активными заказами");
        } else {
            List<Review> reviews = reviewRepository.findByBranchId(branchId);
            for (Review review : reviews) {
                reviewRepository.delete(review);
            }
            branchRepository.delete(branch);
            return;
        }
    }

    @Override
    @CacheEvict(cacheNames = {"branches", "rating_branches"}, allEntries = true)
    public void modify(BranchDtoInput newBranchDto, String branchId) {
        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isPresent()) {
            Branch branch = optionalBranch.get();
            branch.setName(newBranchDto.getName());
            branch.setCity(cityRepository.findById(newBranchDto.getCity()).get());
            branch.setAddress(newBranchDto.getAddress());
            branch.setPhone(newBranchDto.getPhone());
            branchRepository.update(branch);
        } else {
            throw new EntityNotFoundException("Филиал с ID " + branchId + " не найден");
        }

    }

    @Override
    public List<BranchDtoOutput> findByCityAndRating(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Branch> listByCity = branchRepository.findByUserCity(user.getCity().getId());
            Map<Branch, Double> result = new HashMap<>();

            for (Branch branch : listByCity) {
                double rating = branchRepository.findRatingByBranchId(branch.getId());
                result.put(branch, rating);
            }

            List<Map.Entry<Branch, Double>> sortedEntries = result.entrySet()
                    .stream()
                    .sorted(Map.Entry.<Branch, Double>comparingByValue().reversed())
                    .toList();

            return sortedEntries.stream()
                    .map(entry -> {
                        Branch branch = entry.getKey();
                        BranchDtoOutput dto = modelMapper.map(branch, BranchDtoOutput.class);
                        dto.setCity(entry.getKey().getCity().getName());
                        dto.setRating(entry.getValue());

                        List<Review> reviews = reviewRepository.findByBranchId(branch.getId());
                        List<ReviewDtoOutput> lastThreeReviews = reviews.stream()
                                .sorted(Comparator.comparing(Review::getDate).reversed())
                                .limit(3)
                                .map(review -> {
                                    ReviewDtoOutput reviewDto = modelMapper.map(review, ReviewDtoOutput.class);
                                    String username = userRepository.findNameByReview(review.getId());
                                    reviewDto.setUserName(username);
                                    return reviewDto;
                                })
                                .toList();

                        dto.setReviews(lastThreeReviews);


                        return dto;
                    })
                    .toList();
        }
        else {
            throw new EntityNotFoundException("Пользователь " + email + " не найден");
        }
    }

    @Override
    @Cacheable("rating_branches")
    public List<BranchDtoOutput> findAllBranchesSortedByRating() {
        List<Branch> allBranches = branchRepository.getAll();

        Map<Branch, Double> result = new HashMap<>();

        for (Branch branch : allBranches) {
            double rating = branchRepository.findRatingByBranchId(branch.getId());
            result.put(branch, rating);
        }

        List<Map.Entry<Branch, Double>> sortedEntries = result.entrySet()
                .stream()
                .sorted(Map.Entry.<Branch, Double>comparingByValue().reversed())
                .toList();

        return sortedEntries.stream()
                .map(entry -> {
                    Branch branch = entry.getKey();
                    BranchDtoOutput dto = modelMapper.map(branch, BranchDtoOutput.class);
                    dto.setCity(branch.getCity().getId());
                    dto.setRating(entry.getValue());

                    List<Review> reviews = reviewRepository.findByBranchId(branch.getId());
                    List<ReviewDtoOutput> lastThreeReviews = reviews.stream()
                            .sorted(Comparator.comparing(Review::getDate).reversed())
                            .limit(3)
                            .map(review -> {
                                ReviewDtoOutput reviewDto = modelMapper.map(review, ReviewDtoOutput.class);
                                String username = userRepository.findNameByReview(review.getId());
                                reviewDto.setUserName(username);
                                return reviewDto;
                            })
                            .toList();

                    dto.setReviews(lastThreeReviews);


                    return dto;
                })
                .toList();
    }

}
