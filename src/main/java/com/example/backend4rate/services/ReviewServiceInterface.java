package com.example.backend4rate.services;

import java.util.List;

import com.example.backend4rate.exceptions.EmptyResultDataAccessException;
import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.Review;
import com.example.backend4rate.models.dto.ReviewResponse;


public interface ReviewServiceInterface {

     public ReviewResponse addReview(Integer restaurantId, Review review) throws NotFoundException;

    public void deleteReviewById(Integer reviewId) throws EmptyResultDataAccessException ;

    public ReviewResponse getReviewById(Integer reviewId) throws NotFoundException;

    public List<ReviewResponse> getAllReviews(Integer restauranId) throws NotFoundException;

    public Double getAverageRating(Integer restaurantId) ;



}
