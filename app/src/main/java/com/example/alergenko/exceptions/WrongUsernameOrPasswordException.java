package com.example.alergenko.exceptions;

public class WrongUsernameOrPasswordException extends Exception{
    public WrongUsernameOrPasswordException(String message){
        super(message);
    }
}
