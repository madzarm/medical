package com.example.medicalapi.exception.exceptions;

public class UsersApiConnectionRefusedException extends RuntimeException{
    public UsersApiConnectionRefusedException() {
        super("Users-api refused to connect!");
    }
}
