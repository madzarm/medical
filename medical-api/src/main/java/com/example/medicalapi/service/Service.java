package com.example.medicalapi.service;

import com.example.medicalapi.domain.dto.DiseaseDTO;
import com.example.medicalapi.domain.MedicalRecord;
import com.example.medicalapi.domain.dto.MedicalRecordDto;
import com.example.medicalapi.domain.dto.PersonDTO;
import com.example.medicalapi.domain.repository.MedicalRecordRepository;
import com.example.medicalapi.service.result.DataResult;
import com.example.medicalapi.service.result.SearchMedicalRecordResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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

    public DataResult<SearchMedicalRecordResult> fetchAndLoad() throws ExecutionException, InterruptedException {
       CompletableFuture<PersonDTO[]> peopleResult = fetchData(PersonDTO[].class);
       CompletableFuture<DiseaseDTO[]> diseasesResult = fetchData(DiseaseDTO[].class);
       CompletableFuture.allOf(peopleResult,diseasesResult).join();

       List<PersonDTO> peopleDTOs = Arrays.asList(peopleResult.get());
       List<DiseaseDTO> diseaseDTOs = Arrays.asList(diseasesResult.get());
       if(peopleDTOs.isEmpty())
           return new DataResult(false, HttpStatus.NOT_FOUND, "Fetching user data wasn't successful", null);
       else if (diseaseDTOs.isEmpty())
           return new DataResult<>(false, HttpStatus.NOT_FOUND,"Fethcing diseases data wasn't successful",null);

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

       SearchMedicalRecordResult searchMedicalRecordResult = new SearchMedicalRecordResult();
       List<MedicalRecordDto> medicalRecordDtos = mapToDto(medicalRecords);
       searchMedicalRecordResult.setMedicalRecords(medicalRecordDtos);

       return new DataResult<>(true, "Successfully fetched data!", searchMedicalRecordResult);
    }
    @Async("taskExecutor")
    public <T> CompletableFuture<T> fetchData(Class<T> className){
        RestTemplate restTemplate = new RestTemplate();
        String uri = className== PersonDTO[].class ? fetchPeopleUri : fetchDiseasesUri;
        JSONObject[] response = restTemplate.getForObject(uri, JSONObject[].class);
        T result = jsonToPojo(response, className);
        return CompletableFuture.completedFuture(result);
    }

    private <T> T jsonToPojo(JSONObject[] jsonObject, Class<T> className ){
        ObjectMapper mapper = new ObjectMapper().registerModule(new JsonOrgModule());
        return mapper.convertValue(jsonObject,className);
    }



    private List<MedicalRecordDto> mapToDto(List<MedicalRecord> medicalRecords){
        return medicalRecords.stream().map(m ->
                MedicalRecordDto.builder()
                        .age(m.getAge())
                        .weight(m.getWeight())
                        .firstName(m.getFirstName())
                        .lastName(m.getLastName())
                        .diseases(m.getDiseases()).build()
        ).collect(Collectors.toList());
    }
}
