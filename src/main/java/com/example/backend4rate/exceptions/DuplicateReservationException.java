package com.example.backend4rate.exceptions;

public class DuplicateReservationException extends Exception{
    private String className;

    public DuplicateReservationException(String message, String className) {
        super(message);
        this.className = className;
    }

    public DuplicateReservationException(String className) {
        super("Reservation was already made!");
        this.className = className;
    }

    public String getClassName(){
        return className;
    }
}
