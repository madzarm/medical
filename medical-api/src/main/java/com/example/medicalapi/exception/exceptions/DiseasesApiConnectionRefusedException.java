package com.example.medicalapi.exception.exceptions;

public class DiseasesApiConnectionRefusedException extends RuntimeException{
    public DiseasesApiConnectionRefusedException() {
        super("Diseases-api refused to connect!");
    }
}
