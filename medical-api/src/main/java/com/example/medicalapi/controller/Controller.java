package com.example.medicalapi.controller;

import com.example.medicalapi.domain.MedicalRecord;
import com.example.medicalapi.service.Service;
import com.example.medicalapi.service.result.DataResult;
import org.springframework.boot.actuate.health.Health;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class Controller {

    private final Service service;

    public Controller(Service service) {
        this.service = service;
    }

    @GetMapping("/load")
    public void fetchAndLoad() throws ExecutionException, InterruptedException {
        service.fetchAndLoad();
    }

}