package com.example.medicalapi.exception.exceptions;

public class ConnectionRefusedException extends RuntimeException{
    public ConnectionRefusedException() {
        super("Downstream api refused to connect!");
    }
}
