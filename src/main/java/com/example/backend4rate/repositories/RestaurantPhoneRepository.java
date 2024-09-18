package com.example.backend4rate.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend4rate.models.entities.RestaurantEntity;
import com.example.backend4rate.models.entities.RestaurantPhoneEntity;

@Repository
public interface RestaurantPhoneRepository extends JpaRepository<RestaurantPhoneEntity, Integer> {
    List<RestaurantPhoneEntity> findAllByRestaurant(RestaurantEntity restaurant);
}
