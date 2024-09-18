package com.example.backend4rate.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.backend4rate.exceptions.EmptyResultDataAccessException;
import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.Review;
import com.example.backend4rate.models.dto.ReviewResponse;
import com.example.backend4rate.models.entities.RestaurantEntity;
import com.example.backend4rate.models.entities.ReviewEntity;
import com.example.backend4rate.repositories.RestaurantRepository;
import com.example.backend4rate.repositories.ReviewRepository;
import com.example.backend4rate.services.ReviewServiceInterface;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class ReviewService  implements ReviewServiceInterface{

    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public ReviewService(RestaurantRepository restaurantRepository, ReviewRepository reviewRepository, ModelMapper modelMapper) {
        this.reviewRepository = reviewRepository;
        this.modelMapper = modelMapper;
        this.restaurantRepository = restaurantRepository;
    }


    @Override  //QA Da li parametri da budu id restorana, gosta i taj dto review ili sve spakovano u review dto? 
    public ReviewResponse addReview(Integer restaurantId, Review review) throws NotFoundException {
        ReviewEntity reviewEntity = modelMapper.map(review, ReviewEntity.class);
        reviewEntity.setId(null);
        reviewEntity.setRestaurant(restaurantRepository.findById(restaurantId).orElseThrow(NotFoundException::new));
        reviewEntity = reviewRepository.saveAndFlush(reviewEntity);

        return modelMapper.map(review, ReviewResponse.class);
    }

    @Override
    public void deleteReviewById(Integer reviewId) throws EmptyResultDataAccessException {
        reviewRepository.deleteById(reviewId);
    }

    @Override
    public ReviewResponse getReviewById(Integer reviewId) throws NotFoundException {
        return modelMapper.map(reviewRepository.findById(reviewId).orElseThrow(NotFoundException::new),
        ReviewResponse.class);
    }

    @Override
    public List<ReviewResponse> getAllReviews(Integer restaurantId) throws NotFoundException {
        RestaurantEntity restaurantEntity = restaurantRepository.findById(restaurantId)
                .orElseThrow(NotFoundException::new);
        return reviewRepository.findByRestaurant(restaurantEntity).stream().map(l -> modelMapper.map(l, ReviewResponse.class))
                .collect(Collectors.toList());
     }

    @Override
    public Double getAverageRating(Integer restaurantId) {
        return reviewRepository.getAverageRating(restaurantId);
    }

}
