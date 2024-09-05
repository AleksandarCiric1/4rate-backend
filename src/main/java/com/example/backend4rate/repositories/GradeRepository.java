package com.example.backend4rate.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.backend4rate.models.entities.GradeEntity;

@Repository
public interface GradeRepository extends JpaRepository<GradeEntity, Integer>{
    
@Query("SELECT AVG(g.value) FROM GradeEntity g WHERE g.restaurant.id = :restaurantId")
Double getAverageRating(@Param("restaurantId") Integer restaurantId);

List<GradeEntity> findByGuestId(Integer guestId);

}
