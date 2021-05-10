package com.example.alergenko.exceptions;

public class PhoneNumberDoesNotExistInDB extends Exception{
    public PhoneNumberDoesNotExistInDB(String message){
        super(message);
    }
}
