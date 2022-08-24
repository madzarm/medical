package com.example.medicalapi.service.result;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ActionResult {

    private boolean success;
    private HttpStatus status;
    private String message;

    public ActionResult(boolean success, HttpStatus status, String message) {
        this.success = success;
        this.status = status;
        this.message = message;
    }

    public ActionResult(boolean success, String message) {
        this.success = success;
        this.message = message;
        determineStatus(success);
    }

    public <T>ResponseEntity<T> intoResponseEntity() {
        if (success)
            return  (ResponseEntity<T>) ResponseEntity.ok(this);

        return (ResponseEntity<T>)ResponseEntity.status(status).body(this);
    }
    private void determineStatus(boolean success){
        if(success)
            this.status = HttpStatus.OK;
        else
            this.status = HttpStatus.CONFLICT;
    }



    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}