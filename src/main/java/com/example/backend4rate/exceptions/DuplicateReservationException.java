package com.example.backend4rate.exceptions;

public class DuplicateReservationException extends Exception{
    public DuplicateReservationException(String message) {
        super(message);
    }

    public DuplicateReservationException() {
        super("Reservation was already made!");
    }

}
