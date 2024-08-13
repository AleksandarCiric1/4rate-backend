package com.example.backend4rate.services.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.example.backend4rate.models.entities.LogEntity;
import com.example.backend4rate.models.enums.LogLevel;
import com.example.backend4rate.repositories.LogRepository;
import com.example.backend4rate.services.LogServiceInterface;

@Service
public class LogService implements LogServiceInterface{

    private final LogRepository logRepository;

    LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    private void log(String message, LogLevel level, String logger){
        LogEntity logEntity = new LogEntity();
        logEntity.setMessage(message);
        logEntity.setDate(new Date());
        logEntity.setLevel(level.toString());
        logEntity.setLogger(logger);
        this.logRepository.saveAndFlush(logEntity);
    }

    @Override
    public void error(String message, String logger) {
        this.log(message, LogLevel.ERROR, logger);
    }

    @Override
    public void info(String message, String logger) {
        this.log(message, LogLevel.INFO, logger);
    }

    @Override
    public void warning(String message, String logger) {
        this.log(message, LogLevel.WARNING, logger);
    }

}
