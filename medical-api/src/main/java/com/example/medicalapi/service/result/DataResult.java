package com.example.medicalapi.service.result;

import org.springframework.http.HttpStatus;

public class DataResult<T> extends ActionResult{
    private T data;

    public DataResult(boolean success, String message, T data) {
        super(success, message);
        this.data = data;
    }

    public DataResult(boolean success, HttpStatus status, String message, T data) {
        super(success, status, message);
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}