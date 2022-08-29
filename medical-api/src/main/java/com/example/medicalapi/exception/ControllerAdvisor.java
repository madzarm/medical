package com.example.medicalapi.exception;

import com.example.medicalapi.exception.exceptions.ConnectionRefusedException;
import com.example.medicalapi.exception.exceptions.PersonNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConnectionRefusedException.class)
    public ResponseEntity<Object> handleConnectionRefusedException(
            ConnectionRefusedException ex, WebRequest request) {

        Map<String, Object> body = createBody(ex.getMessage(),HttpStatus.CONFLICT);

        return new ResponseEntity<>(body,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<Object> handlePersonNotFoundException(
            PersonNotFoundException ex, WebRequest request) {

        Map<String,Object> body = createBody(ex.getMessage(),HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(body,HttpStatus.NOT_FOUND);
    }

    private Map<String,Object> createBody(String message, HttpStatus status){
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status);
        body.put("message", message);
        return body;
    }
}
