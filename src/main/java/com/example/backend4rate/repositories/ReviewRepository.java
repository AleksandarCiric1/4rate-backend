package com.example.backend4rate.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.backend4rate.models.entities.RestaurantEntity;
import com.example.backend4rate.models.entities.ReviewEntity;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer>{

        Collection<ReviewEntity> findByRestaurant(RestaurantEntity restaurantEntity);

        @Query("SELECT AVG(g.grade) FROM ReviewEntity g WHERE g.restaurant.id = :restaurantId")
        Double getAverageRating(@Param("restaurantId") Integer restaurantId);
}
