package com.example.medicalapi.controller;

import com.example.medicalapi.service.Service;
import com.example.medicalapi.service.request.CreateDiseaseHistoryRequest;
import com.example.medicalapi.service.request.CreateDiseaseRequest;
import com.example.medicalapi.service.request.CreateMedicalRecordRequest;
import com.example.medicalapi.service.result.ActionResult;
import com.example.medicalapi.service.result.DataResult;
import com.example.medicalapi.service.result.SearchMedicalRecordResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
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
    @PostMapping("/medical-record")
    public ResponseEntity<ActionResult> createMedicalRecord(@Valid @RequestBody CreateMedicalRecordRequest request) throws ExecutionException, InterruptedException {
        return service.createMedicalRecord(request).intoResponseEntity();
    }
    @PostMapping("/person/diseaseHistory")
    public ResponseEntity<ActionResult> createNewDiseaseHistory(@Valid @RequestBody CreateDiseaseHistoryRequest request) throws ExecutionException, InterruptedException {
        return service.createDiseaseHistory(request).intoResponseEntity();
    }
    @PostMapping("/diseases")
    public ResponseEntity<ActionResult> createNewDisease(@Valid @RequestBody CreateDiseaseRequest request) {
        return service.createDisease(request).intoResponseEntity();
    }
    @GetMapping("/medical-record")
    @Cacheable("person")
    public ResponseEntity<DataResult<SearchMedicalRecordResult>> findMedicalRecords(
            @RequestParam(required = false) Integer personId,
            @RequestParam(required = false) Integer diseaseId,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String diseaseName,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate to
            ) throws ExecutionException, InterruptedException {

        boolean hasPersonIdSearch = Objects.nonNull(personId);
        boolean hasDiseaseIdSearch = Objects.nonNull(diseaseId);
        boolean hasNameSearch = Objects.nonNull(firstName) || Objects.nonNull(lastName);
        boolean hasDiseaseNameSearch = Objects.nonNull(diseaseName);
        boolean hasDateSearch = Objects.nonNull(from) || Objects.nonNull(to);

        if(hasPersonIdSearch ? (hasDiseaseNameSearch || hasDiseaseIdSearch || hasNameSearch) :
                (hasDiseaseIdSearch ? (hasNameSearch || hasDiseaseNameSearch) :
                        (hasDiseaseNameSearch ?(hasNameSearch || hasDateSearch) : (hasDateSearch && hasNameSearch))))
            return new DataResult<>(false,"You can combine only name searches (firstName & lastName), " +
                    "and date searches (from & to). Other searches can not be combined with each other!",null).intoResponseEntity();
        else if (hasPersonIdSearch)
            return service.findByPersonId(personId).intoResponseEntity();
        else if(hasDiseaseIdSearch)
            return service.findByDiseaseId(diseaseId).intoResponseEntity();
        else if(hasDiseaseNameSearch)
            return service.findByDiseaseName(diseaseName).intoResponseEntity();
        else if(hasDateSearch)
            return service.findByDate(from,to).intoResponseEntity();
        else if(hasNameSearch)
            return service.findByName(firstName,lastName).intoResponseEntity();
        else return service.findAll().intoResponseEntity();
    }



}