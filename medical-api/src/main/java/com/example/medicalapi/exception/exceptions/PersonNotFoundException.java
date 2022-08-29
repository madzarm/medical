package com.example.medicalapi.exception.exceptions;

public class PersonNotFoundException extends RuntimeException {
    public PersonNotFoundException(int id) {
        super("Person with id: " + id + " not found!");
    }
}
