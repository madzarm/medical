package com.example.medicalapi.exception;

import com.example.medicalapi.exception.exceptions.DiseasesApiConnectionRefusedException;
import com.example.medicalapi.exception.exceptions.EmptyResponseException;
import com.example.medicalapi.exception.exceptions.PersonNotFoundException;
import com.example.medicalapi.exception.exceptions.UsersApiConnectionRefusedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor{


    @ExceptionHandler({DiseasesApiConnectionRefusedException.class, UsersApiConnectionRefusedException.class})
    public ResponseEntity<Object> handleConnectionRefusedException(RuntimeException ex) {
        return handleException(ex,HttpStatus.BAD_GATEWAY);
    }
    @ExceptionHandler({PersonNotFoundException.class, EmptyResponseException.class})
    public ResponseEntity<Object> handleNotFoundException (RuntimeException ex) {
        return handleException(ex,HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<Object> handleException(RuntimeException ex, HttpStatus status){
        Map<String,Object> body = createBody(ex.getMessage(),status);
        return new ResponseEntity<>(body,status);
    }

    private Map<String,Object> createBody(String message, HttpStatus status){
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status);
        body.put("message", message);
        return body;
    }
}
