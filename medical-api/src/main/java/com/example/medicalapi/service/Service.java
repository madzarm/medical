package com.example.medicalapi.service;

import com.example.medicalapi.domain.DiseaseDTO;
import com.example.medicalapi.domain.MedicalRecord;
import com.example.medicalapi.domain.PersonDTO;
import com.example.medicalapi.domain.repository.MedicalRecordRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${fetch.people.endpoint.uri}")
    private String fetchPeopleUri;
    @Value("${fetch.diseases.endpoint.uri}")
    private String fetchDiseasesUri;

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
        RestTemplate restTemplate = new RestTemplate();
        JSONObject[] response = restTemplate.getForObject(fetchPeopleUri, JSONObject[].class);
        PersonDTO[] result = jsonToPojo(response, PersonDTO[].class);
        return CompletableFuture.completedFuture(result);
    }
    @Async("taskExecutor")
    public CompletableFuture<DiseaseDTO[]> fetchDiseases() {
        RestTemplate restTemplate = new RestTemplate();
        JSONObject[] response = restTemplate.getForObject(fetchDiseasesUri, JSONObject[].class);
        DiseaseDTO[] result = jsonToPojo(response, DiseaseDTO[].class);
        return CompletableFuture.completedFuture(result);
    }

    private <T> T jsonToPojo(JSONObject[] jsonObject, Class<T> className ){
        ObjectMapper mapper = new ObjectMapper().registerModule(new JsonOrgModule());
        return mapper.convertValue(jsonObject,className);
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
