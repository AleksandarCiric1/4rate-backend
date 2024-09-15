package com.example.backend4rate.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.backend4rate.exceptions.InvalidGradeException;
import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.Grade;
import com.example.backend4rate.models.entities.GradeEntity;
import com.example.backend4rate.repositories.GradeRepository;
import com.example.backend4rate.repositories.GuestRepository;
import com.example.backend4rate.repositories.RestaurantRepository;
// import com.example.backend4rate.repositories.StandardUserRepository;
import com.example.backend4rate.repositories.UserAccountRepository;
import com.example.backend4rate.services.RateServiceInterface;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class RateService implements RateServiceInterface {
    private final GradeRepository gradeRepository;
    private final RestaurantRepository restaurantRepository;
    private final GuestRepository guestRepository;
    private final ModelMapper modelMapper;
    @PersistenceContext
    private EntityManager entityManager;

    public RateService(GradeRepository gradeRepository, RestaurantRepository restaurantRepository,
            UserAccountRepository userAccountRepository,
            GuestRepository guestRepository, GuestRepository guestRepository2, GradeRepository gradeRepository2,
            ModelMapper modelMapper, EntityManager entityManager) {
        this.restaurantRepository = restaurantRepository;
        this.gradeRepository = gradeRepository;
        this.guestRepository = guestRepository;
        this.modelMapper = modelMapper;
        this.entityManager = entityManager;
    }

    @Override
    public Grade addRating(Grade grade) throws InvalidGradeException, NotFoundException {
        if (grade.getValue() > 5 || grade.getValue() < 1)
            throw new InvalidGradeException("Grade " + grade.getValue() + " is not in allowed range!");
        if (!guestRepository.findById(grade.getGuestId()).isPresent())
            throw new NotFoundException();
        if (!restaurantRepository.findById(grade.getRestaurantId()).isPresent())
            throw new NotFoundException();
        Integer vGrade = null;
        if (!gradeRepository.findByGuestId(grade.getGuestId()).stream()
                .noneMatch(g -> g.getRestaurant().getId().equals(grade.getRestaurantId())))
            vGrade = gradeRepository.findByGuestId(grade.getGuestId()).stream()
                    .filter(g -> g.getRestaurant().getId().equals(grade.getRestaurantId()))
                    .findFirst()
                    .orElse(null).getId();

        GradeEntity gradeEntity = modelMapper.map(grade, GradeEntity.class);
        gradeEntity.setId(vGrade);
        gradeEntity = gradeRepository.saveAndFlush(gradeEntity);
        // Samo gost moze ocijenti, otuda upotreba pravog ID Gosta
        return modelMapper.map(gradeEntity, Grade.class);
    }

    @Override
    public void deleteRating(Integer gradeId) {
        gradeRepository.deleteById(gradeId);
    }

    @Override
    public Double getTotalRating(Integer restaurantId) {
        return gradeRepository.getAverageRating(restaurantId);
    }

}
