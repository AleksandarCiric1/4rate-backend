package com.example.backend4rate.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend4rate.exceptions.EmptyResultDataAccessException;
import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.Review;
import com.example.backend4rate.models.dto.ReviewResponse;
import com.example.backend4rate.services.impl.ReviewService;

@RestController
@RequestMapping("/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService){
        this.reviewService=reviewService;
    }

    @PostMapping("/add/{restaurantId}")
    public ReviewResponse addReview(@PathVariable Integer restaurantId, @RequestBody Review review) throws NotFoundException{
        return reviewService.addReview(restaurantId, review);
    }

    @GetMapping("/get/{reviewId}")
    public ReviewResponse getReview(@PathVariable Integer reviewId) throws NotFoundException
    {
       return reviewService.getReviewById(reviewId);
    }
        

    @GetMapping("/getAll/{restaurantId}")
    public List<ReviewResponse> getReviews(@PathVariable Integer restaurantId) throws NotFoundException {
        return this.reviewService.getAllReviews(restaurantId);
    }

    @GetMapping("/avgGrade/{RestaurantId}")
    public Double getAverageGrade(@PathVariable Integer RestaurantId){
        return reviewService.getAverageRating(RestaurantId);
    }

    @DeleteMapping("/delete/{reviewId}")
    public void deleteReview(@PathVariable Integer reviewId) throws EmptyResultDataAccessException {
        reviewService.deleteReviewById(reviewId);
    }

}
