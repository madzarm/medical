package com.example.medicalapi.exception.exceptions;

public class EmptyResponseException extends RuntimeException{
    public EmptyResponseException(String apiName) {
        super(apiName + " has returned an empty response!");
    }
}
