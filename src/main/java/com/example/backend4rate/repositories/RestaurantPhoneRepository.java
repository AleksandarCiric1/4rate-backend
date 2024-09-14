package com.example.backend4rate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend4rate.models.entities.RestaurantPhoneEntity;
import com.example.backend4rate.models.entities.RestaurantEntity;
import java.util.List;

@Repository
public interface RestaurantPhoneRepository extends JpaRepository<RestaurantPhoneEntity, Integer>{

	List<RestaurantPhoneEntity> findByRestaurant(RestaurantEntity restaurant);
    
}
