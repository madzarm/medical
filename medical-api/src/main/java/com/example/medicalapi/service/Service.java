package com.example.medicalapi.service;

import com.example.medicalapi.domain.DiseaseDTO;
import com.example.medicalapi.domain.MedicalRecord;
import com.example.medicalapi.domain.PersonDTO;
import com.example.medicalapi.domain.repository.MedicalRecordRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;
import org.json.simple.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class Service {

    private final MedicalRecordRepository medicalRecordRepository;

    public Service(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public void fetchAndLoad() throws ExecutionException, InterruptedException {
       CompletableFuture<PersonDTO[]> peopleResult = fetchPeople();
       CompletableFuture<DiseaseDTO[]> diseasesResult = fetchDiseases();
       CompletableFuture.allOf(peopleResult,diseasesResult).join();

       List<PersonDTO> peopleDTOs = Arrays.asList(peopleResult.get());
       List<DiseaseDTO> diseaseDTOs = Arrays.asList(diseasesResult.get());

       List<MedicalRecord> medicalRecords = peopleDTOs.stream().map(p -> {
           Optional<DiseaseDTO> diseaseDTOOptional = diseaseDTOs.stream().filter(d -> d.getUserid()==p.getUserid()).findFirst();
           String diseases = "";
           if(diseaseDTOOptional.isPresent()){
               diseases = diseaseDTOOptional.get().getDiseases();
           }
           List<String> diseaseList = new ArrayList<>();
           if(diseases.contains(",")){
               String[] diseasesArray = diseases.split(", ");
               diseaseList = (Arrays.asList(diseasesArray));
           } else if (!diseases.isEmpty()) {
               diseaseList = (List.of(diseases));
           }
           return MedicalRecord.builder()
                   .age(p.getAge())
                   .userId(p.getUserid())
                   .firstName(p.getName())
                   .lastName(p.getSurname())
                   .weight(p.getWeight())
                   .diseases(diseaseList).build();
       }).collect(Collectors.toList());

       medicalRecordRepository.saveAll(medicalRecords);
    }

    @Async("taskExecutor")
    public CompletableFuture<PersonDTO[]> fetchPeople() {
        String uri = "http://localhost:8081/api/people";
        RestTemplate restTemplate = new RestTemplate();
        JSONObject[] response = restTemplate.getForObject(uri, JSONObject[].class);
        PersonDTO[] result = jsonToPersonResult(response);
        return CompletableFuture.completedFuture(result);
    }
    @Async("taskExecutor")
    public CompletableFuture<DiseaseDTO[]> fetchDiseases() {
        String uri = "http://localhost:8082/api/records";
        RestTemplate restTemplate = new RestTemplate();
        JSONObject[] response = restTemplate.getForObject(uri, JSONObject[].class);
        DiseaseDTO[] result = jsonToDiseaseResult(response);
        return CompletableFuture.completedFuture(result);
    }

    private <T> T jsonToPojo(JSONObject jsonObject, Class<T> className ){
        ObjectMapper mapper = new ObjectMapper().registerModule(new JsonOrgModule());
        return mapper.convertValue(jsonObject,className);
    }

    private PersonDTO[] jsonToPersonResult(JSONObject[] jsonObject){
        ObjectMapper mapper = new ObjectMapper().registerModule(new JsonOrgModule());
        return mapper.convertValue(jsonObject, PersonDTO[].class);
    }

    private DiseaseDTO[] jsonToDiseaseResult(JSONObject[] jsonObject){
        ObjectMapper mapper = new ObjectMapper().registerModule(new JsonOrgModule());
        return mapper.convertValue(jsonObject, DiseaseDTO[].class);
    }

    @Async("taskExecutor")
    public CompletableFuture<String> fetchUserInfo(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("User");
        return CompletableFuture.completedFuture("User");
    }
    @Async
    public CompletableFuture<String> fetchMedicalInfo(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Medical");
        return CompletableFuture.completedFuture("Medical");
    }
}
