package com.example.backend4rate.exceptions;

public class ManagerBlockedException extends Exception {
    public ManagerBlockedException(){
        super("Action forbidden Manager is suspended!");
    }

    public ManagerBlockedException(String msg){
        super(msg);
    }
}
