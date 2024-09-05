package com.example.backend4rate.services;

import com.example.backend4rate.exceptions.InvalidGradeException;
import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.Grade;

public interface RateServiceInterface {
    public Grade addRating(Grade grade) throws InvalidGradeException, NotFoundException;

    public void deleteRating(Integer gradeId);

    public Double getTotalRating(Integer restaurantId);


}
