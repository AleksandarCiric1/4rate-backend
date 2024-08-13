package com.example.backend4rate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend4rate.models.entities.ImageEntity;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Integer>{

}
