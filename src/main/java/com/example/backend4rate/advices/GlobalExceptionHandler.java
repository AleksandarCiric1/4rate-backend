package com.example.backend4rate.advices;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.backend4rate.exceptions.NullPointerException;
import com.example.backend4rate.exceptions.BadRequestException;
import com.example.backend4rate.exceptions.DuplicateReservationException;
import com.example.backend4rate.exceptions.EmptyResultDataAccessException;
import com.example.backend4rate.exceptions.InvalidGradeException;
import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.exceptions.ReservationsFullException;
import com.example.backend4rate.exceptions.UnauthorizedException;
import com.example.backend4rate.services.impl.LogService;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final LogService logService;

    public GlobalExceptionHandler(LogService logService){
        this.logService = logService;
    }
    
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException ex){
        logService.info(ex.getMessage(), ex.getClassName());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex){
        logService.info(ex.getMessage(), ex.getClassName());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex){
        logService.info(ex.getMessage(), ex.getClassName());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidGradeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleInvalidGradeException(InvalidGradeException ex){
        logService.info(ex.getMessage(), ex.getClassName());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateReservationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleDuplicateReservationException(DuplicateReservationException ex){
        logService.info(ex.getMessage(), ex.getClassName());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleBadRequestException(BadRequestException ex){
        logService.info(ex.getMessage(), ex.getClassName());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleNullPointerException(NullPointerException ex){
        logService.info(ex.getMessage(), ex.getClassName());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleIOException(IOException ex){
        logService.info(ex.getMessage(), null);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ReservationsFullException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleReservationsFullException(ReservationsFullException ex){
        logService.info(ex.getMessage(), ex.getClassName());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
