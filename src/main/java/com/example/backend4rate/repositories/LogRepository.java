package com.example.backend4rate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend4rate.models.entities.LogEntity;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, Integer>{

}
