package com.example.medicalapi.controller;

import com.example.medicalapi.service.Service;
import com.example.medicalapi.service.result.DataResult;
import com.example.medicalapi.service.result.SearchMedicalRecordResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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

    @GetMapping("/api-docs/users")
    private String getusers()
    {
        final String uri = "http://localhost:8081/api-docs";

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);

        return result;
    }

    @GetMapping("/api-docs/diseases")
    private String getdiseases()
    {
        final String uri = "http://localhost:8082/api-docs";

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);

        return result;
    }

    @GetMapping("/docs")
    @ResponseBody
    public String servers(){
        JSONObject server = new JSONObject();
        JSONObject url = new JSONObject();
        JSONObject url1 = new JSONObject();
        JSONObject url2 = new JSONObject();
        JSONObject info = new JSONObject();
        ArrayList megaurl = new ArrayList();
        url.put("url", "http://localhost:8082/");
        url.put("description", "Dieses");
        url1.put("url", "http://localhost:8081/");
        url1.put("description", "Users");
        url2.put("url", "http://localhost:8080/api/v1/");
        url2.put("description", "Main");
        megaurl.add(url);
        megaurl.add(url1);
        megaurl.add(url2);
        info.append("title","OpenAPI definition");
        info.append("version","1");
        server.put("info", info);
        server.put("openapi", "3.0.1");
        server.put("servers", megaurl);
        String response = server.toString();
        return response;
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
        else return new DataResult<>(false,"has nothing",null).intoResponseEntity();
    }



}