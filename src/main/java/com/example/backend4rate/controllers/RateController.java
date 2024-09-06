package com.example.backend4rate.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend4rate.exceptions.InvalidGradeException;
import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.Grade;
import com.example.backend4rate.services.impl.RateService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/v1/grades")
public class RateController {

    private final RateService rateService;

    public RateController(RateService rateService){
        this.rateService=rateService;
    }

    @PostMapping("/addGrade")
    public Grade addGrade(@RequestBody Grade grade) throws InvalidGradeException, NotFoundException{
        return rateService.addRating(grade);
    }

    @GetMapping("/avgGrade/{RestaurantId}")
    public Double getAverageGrade(@PathVariable Integer RestaurantId){
        return rateService.getTotalRating(RestaurantId);
    }
    @DeleteMapping("/deleteGrade/{gradeId}")
    public void deleteGrade(@PathVariable Integer gradeId){
         rateService.deleteRating(gradeId);
    }
    
    
}
