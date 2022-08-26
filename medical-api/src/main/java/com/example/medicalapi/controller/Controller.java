package com.example.medicalapi.controller;

import com.example.medicalapi.domain.MedicalRecord;
import com.example.medicalapi.service.Service;
import com.example.medicalapi.service.result.ActionResult;
import com.example.medicalapi.service.result.DataResult;
import com.example.medicalapi.service.result.SearchMedicalRecordResult;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class Controller {

    private final Service service;

    public Controller(Service service) {
        this.service = service;
    }

    @GetMapping("/load")
    @Cacheable("data")
    public ResponseEntity<DataResult<SearchMedicalRecordResult>> fetchAndLoad() throws ExecutionException, InterruptedException {
        return service.fetchAndLoad().intoResponseEntity();
    }

}