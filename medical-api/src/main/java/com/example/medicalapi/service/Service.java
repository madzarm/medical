package com.example.medicalapi.service;

import com.example.medicalapi.domain.DiseaseHistory;
import com.example.medicalapi.domain.MedicalRecord;
import com.example.medicalapi.domain.dto.DiseaseHistoryDto;
import com.example.medicalapi.domain.dto.MedicalRecordDto;
import com.example.medicalapi.domain.dto.PersonDTO;
import com.example.medicalapi.domain.repository.MedicalRecordRepository;
import com.example.medicalapi.exception.exceptions.DiseasesApiConnectionRefusedException;
import com.example.medicalapi.exception.exceptions.EmptyResponseException;
import com.example.medicalapi.exception.exceptions.UsersApiConnectionRefusedException;
import com.example.medicalapi.service.response.DiseaseHistoryResponse;
import com.example.medicalapi.service.response.DiseaseResponse;
import com.example.medicalapi.service.response.PersonResponse;
import com.example.medicalapi.service.result.DataResult;
import com.example.medicalapi.service.result.SearchMedicalRecordResult;
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
    @Value("${fetch.person.endpoint.uri}")
    private String fetchPersonUri;
    @Value("${fetch.diseases.endpoint.uri}")
    private String fetchDiseasesUri;
    @Value("${fetch.disease.endpoint.uri}")
    private String fetchDiseaseUri;
    @Value("${api.diseases.name}")
    private String diseaseApiName;
    @Value("${api.users.name}")
    private String usersApiName;

    public Service(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public DataResult<SearchMedicalRecordResult> findAll() throws ExecutionException, InterruptedException {
        CompletableFuture<PersonResponse[]> personResponse = fetchData(PersonResponse[].class);
        CompletableFuture<DiseaseResponse[]> diseaseResponse = fetchData(DiseaseResponse[].class);
        CompletableFuture.allOf(diseaseResponse,personResponse).join();

        List<PersonResponse> personResponseList = Arrays.asList(personResponse.get());
        List<DiseaseResponse> diseaseResponseList = Arrays.asList(diseaseResponse.get());

        if(personResponseList.isEmpty())
           throw new EmptyResponseException(usersApiName);
       else if (diseaseResponseList.isEmpty())
           throw new EmptyResponseException(diseaseApiName);
       List<MedicalRecord> medicalRecords = createMedicalRecords(personResponseList,diseaseResponseList);

       SearchMedicalRecordResult searchMedicalRecordResult = new SearchMedicalRecordResult();
       List<MedicalRecordDto> medicalRecordDtos = mapToDto(medicalRecords);
       searchMedicalRecordResult.setMedicalRecords(medicalRecordDtos);

       return new DataResult<>(true, "Successfully fetched data!", searchMedicalRecordResult);

    }

    @Async("taskExecutor")
    public <T> CompletableFuture<T> fetchById(Class<T> className, int id){
        RestTemplate restTemplate = new RestTemplate();
        String uri = (className== PersonDTO.class ? fetchPersonUri : fetchDiseaseUri) + id;
        T response;
        try {
            response = restTemplate.getForObject(uri, className);
        }
        catch (Exception e){
            if(className == PersonDTO.class)
                throw new UsersApiConnectionRefusedException();
            throw new DiseasesApiConnectionRefusedException();
        }
        return CompletableFuture.completedFuture(response);
    }

    @Async("taskExecutor")
    public <T> CompletableFuture<T> fetchData(Class<T> className){
        RestTemplate restTemplate = new RestTemplate();
        String uri = className == PersonResponse[].class ? fetchPeopleUri : fetchDiseasesUri;
        T response;
        try {
            response = restTemplate.getForObject(uri, className);
        } catch (Exception e) {
           if(className == PersonDTO[].class)
               throw new UsersApiConnectionRefusedException();
           throw new DiseasesApiConnectionRefusedException();
        }
        return CompletableFuture.completedFuture(response);
    }

    private List<MedicalRecord> createMedicalRecords(List<PersonResponse> personResponses, List<DiseaseResponse> diseaseResponses){
        return personResponses.stream().map(p -> {

            List<DiseaseHistory> diseaseHistories = responseToDiseaseHistory(p.getDiseaseHistories(),diseaseResponses);

            return MedicalRecord.builder()
                    .diseaseHistories(diseaseHistories)
                    .userId(p.getId())
                    .age(p.getAge())
                    .weight(p.getWeight())
                    .firstName(p.getName())
                    .lastName(p.getSurname()).build();
        }).collect(Collectors.toList());
    }

    private List<DiseaseHistory> responseToDiseaseHistory
            (List<DiseaseHistoryResponse> diseaseHistoryResponse, List<DiseaseResponse> diseaseResponse){
        return diseaseHistoryResponse.stream().map(dh -> {
            Optional<DiseaseResponse> diseaseOptional = diseaseResponse.stream().filter(dr -> dr.getId() == dh.getDiseaseId()).findFirst();
            return diseaseOptional.map(response -> DiseaseHistory.builder()
                    .diseaseName(response.getName())
                    .id(dh.getId())
                    .curable(response.isCurable())
                    .dateDiscovered(dh.getDateDiscovered()).build()).orElse(null);
        }).collect(Collectors.toList());
    }


    private List<MedicalRecordDto> mapToDto(List<MedicalRecord> medicalRecords){
        return medicalRecords.stream().map(m ->
            MedicalRecordDto.builder()
                    .age(m.getAge())
                    .weight(m.getWeight())
                    .firstName(m.getFirstName())
                    .lastName(m.getLastName())
                    .diseaseHistories(mapDiseaseHistoryToDto(m.getDiseaseHistories())).build()
        ).collect(Collectors.toList());
    }

    private List<DiseaseHistoryDto> mapDiseaseHistoryToDto(List<DiseaseHistory> diseaseHistories){
        return diseaseHistories.stream().map(dh ->
            DiseaseHistoryDto.builder()
                    .diseaseName(dh.getDiseaseName())
                    .dateDiscovered(dh.getDateDiscovered())
                    .id(dh.getId()).build()
         ).collect(Collectors.toList());
    }
}
