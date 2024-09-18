package com.example.backend4rate.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend4rate.models.entities.ImageEntity;
import com.example.backend4rate.models.entities.RestaurantEntity;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {
    List<ImageEntity> findAllByRestaurant(RestaurantEntity restaurantEntity);
}
