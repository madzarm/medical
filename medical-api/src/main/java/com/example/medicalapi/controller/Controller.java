package com.example.medicalapi.controller;

import com.example.medicalapi.service.Service;
import com.example.medicalapi.service.result.DataResult;
import com.example.medicalapi.service.result.SearchMedicalRecordResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;

@RestController
public class Controller {

    private final Service service;
    private final RestTemplate restTemplate;

    @Autowired
    public Controller(Service service, RestTemplate restTemplate) {
        this.service = service;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/load")
    @Cacheable("people")
    public ResponseEntity<DataResult<SearchMedicalRecordResult>> findAll() throws ExecutionException, InterruptedException {
        return service.findAll().intoResponseEntity();
    }

    @GetMapping("/person")
    @Cacheable("people")
    public ResponseEntity<DataResult<SearchMedicalRecordResult>> findById(@RequestParam int id) {
        return service.findById(id).intoResponseEntity();
    }

}