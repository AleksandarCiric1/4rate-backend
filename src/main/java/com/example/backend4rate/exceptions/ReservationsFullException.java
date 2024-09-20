package com.example.backend4rate.exceptions;

public class ReservationsFullException extends Exception{
    public ReservationsFullException(){
        super("Resource Limit Exceded!");
    }
    public ReservationsFullException(String message){
        super(message);
    }


}
