package com.example.backend4rate.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.backend4rate.exceptions.EmptyResultDataAccessException;
import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.Review;
import com.example.backend4rate.models.dto.ReviewResponse;
import com.example.backend4rate.models.entities.GuestEntity;
import com.example.backend4rate.models.entities.RestaurantEntity;
import com.example.backend4rate.models.entities.ReviewEntity;
import com.example.backend4rate.repositories.GuestRepository;
import com.example.backend4rate.repositories.RestaurantRepository;
import com.example.backend4rate.repositories.ReviewRepository;
import com.example.backend4rate.services.ReviewServiceInterface;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class ReviewService implements ReviewServiceInterface {

    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final GuestRepository guestRepository;
    private final ModelMapper modelMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public ReviewService(RestaurantRepository restaurantRepository, ReviewRepository reviewRepository,
            GuestRepository guestRepository, ModelMapper modelMapper) {
        this.reviewRepository = reviewRepository;
        this.modelMapper = modelMapper;
        this.restaurantRepository = restaurantRepository;
        this.guestRepository = guestRepository;
    }

    @Override
    public ReviewResponse addReview(Integer restaurantId, Review review) throws NotFoundException {
        GuestEntity guestEntity = guestRepository.findByUserAccount_Id(review.getUserAccountId())
                .orElseThrow(() -> new NotFoundException(ReviewService.class.getName()));
        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setGuest(guestEntity);
        reviewEntity.setId(null);
        reviewEntity.setCreatedAt(new Date());
        reviewEntity.setComment(review.getComment());
        reviewEntity.setGrade(review.getGrade());
        reviewEntity.setRestaurant(restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(ReviewService.class.getName())));
        reviewEntity = reviewRepository.saveAndFlush(reviewEntity);

        return modelMapper.map(review, ReviewResponse.class);
    }

    @Override
    public void deleteReviewById(Integer reviewId) throws EmptyResultDataAccessException {
        reviewRepository.deleteById(reviewId);
    }

    @Override
    public ReviewResponse getReviewById(Integer reviewId) throws NotFoundException {
        return modelMapper.map(
                reviewRepository.findById(reviewId)
                        .orElseThrow(() -> new NotFoundException(ReviewService.class.getName())),
                ReviewResponse.class);
    }

    @Override
    public List<ReviewResponse> getAllReviews(Integer restaurantId) throws NotFoundException {
        RestaurantEntity restaurantEntity = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException(ReviewService.class.getName()));
        return reviewRepository.findByRestaurant(restaurantEntity).stream()
                .map(l -> modelMapper.map(l, ReviewResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public Double getAverageRating(Integer restaurantId) {
        return reviewRepository.getAverageRating(restaurantId);
    }

}
