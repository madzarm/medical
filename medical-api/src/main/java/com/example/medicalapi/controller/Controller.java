package com.example.medicalapi.controller;

import com.example.medicalapi.service.Service;
import com.example.medicalapi.service.result.DataResult;
import com.example.medicalapi.service.result.SearchMedicalRecordResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

@RestController
public class Controller {

    private final Service service;
    //private final RestTemplate restTemplate;

    @Autowired
    public Controller(Service service) {
        this.service = service;
    }

    @GetMapping("/load")
    @Cacheable("people")
    public ResponseEntity<DataResult<SearchMedicalRecordResult>> findAll() throws ExecutionException, InterruptedException {
        return service.findAll().intoResponseEntity();
    }

    @GetMapping("/medical-record")
    @Cacheable("person")
    public ResponseEntity<DataResult<SearchMedicalRecordResult>> findMedicalRecords(
            @RequestParam(required = false) Integer personId,
            @RequestParam(required = false) Integer diseaseId,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String diseaseName
    ) throws ExecutionException, InterruptedException {
        boolean hasPersonIdSearch = Objects.nonNull(personId);
        boolean hasDiseaseIdSearch = Objects.nonNull(diseaseId);
        boolean hasFirstNameSearch = Objects.nonNull(firstName);
        boolean hasLastNameSearch = Objects.nonNull(lastName);
        boolean hasDiseaseNameSearch = Objects.nonNull(diseaseName);

        if(hasPersonIdSearch ? (hasDiseaseNameSearch || hasDiseaseIdSearch || hasFirstNameSearch || hasLastNameSearch) : (hasDiseaseIdSearch ? (hasFirstNameSearch || hasLastNameSearch || hasDiseaseNameSearch) : (hasDiseaseNameSearch && (hasFirstNameSearch || hasLastNameSearch)) ))
            return new DataResult<>(false,"bad",null).intoResponseEntity();
        else if (hasPersonIdSearch)
            return service.findByPersonId(personId).intoResponseEntity();
        else if(hasDiseaseIdSearch)
            return service.findByDiseaseId(diseaseId).intoResponseEntity();
        else if(hasDiseaseNameSearch)
            return service.findByDiseaseName(diseaseName).intoResponseEntity();
        else if(hasLastNameSearch || hasFirstNameSearch)
            return new DataResult<>(false,"has name",null).intoResponseEntity();
        else return service.findAll().intoResponseEntity();
    }



}