package com.example.medicalapi.service;

import com.example.medicalapi.domain.dto.DiseaseDTO;
import com.example.medicalapi.domain.MedicalRecord;
import com.example.medicalapi.domain.dto.MedicalRecordDto;
import com.example.medicalapi.domain.dto.PersonDTO;
import com.example.medicalapi.domain.repository.MedicalRecordRepository;
import com.example.medicalapi.exception.exceptions.DiseasesApiConnectionRefusedException;
import com.example.medicalapi.exception.exceptions.EmptyResponseException;
import com.example.medicalapi.exception.exceptions.PersonNotFoundException;
import com.example.medicalapi.exception.exceptions.UsersApiConnectionRefusedException;
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

       CompletableFuture<PersonDTO[]> peopleResult = fetchData(PersonDTO[].class);
       CompletableFuture<DiseaseDTO[]> diseasesResult = fetchData(DiseaseDTO[].class);
       CompletableFuture.allOf(peopleResult,diseasesResult).join();

       List<PersonDTO> peopleDTOs = Arrays.asList(peopleResult.get());
       List<DiseaseDTO> diseaseDTOs = Arrays.asList(diseasesResult.get());
       if(peopleDTOs.isEmpty())
           throw new EmptyResponseException(usersApiName);
       else if (diseaseDTOs.isEmpty())
           throw new EmptyResponseException(diseaseApiName);

       List<MedicalRecord> medicalRecords = createMedicalRecords(diseaseDTOs,peopleDTOs);

       medicalRecordRepository.saveAll(medicalRecords);

       SearchMedicalRecordResult searchMedicalRecordResult = new SearchMedicalRecordResult();
       List<MedicalRecordDto> medicalRecordDtos = mapToDto(medicalRecords);
       searchMedicalRecordResult.setMedicalRecords(medicalRecordDtos);

       return new DataResult<>(true, "Successfully fetched data!", searchMedicalRecordResult);
    }

    public DataResult<SearchMedicalRecordResult> findById(int id) {
        CompletableFuture<PersonDTO> personResult= fetchById(PersonDTO.class,id);
        CompletableFuture<DiseaseDTO> diseaseResult = fetchById(DiseaseDTO.class, id);
        CompletableFuture.allOf(personResult,diseaseResult).join();
        List<MedicalRecord> medicalRecords;
        try {
             medicalRecords = createMedicalRecords(List.of(diseaseResult.get()),List.of(personResult.get()));
        } catch (Exception e) {
            throw new PersonNotFoundException(id);
        }
        List<MedicalRecordDto> medicalRecordDtos = mapToDto(medicalRecords);

        SearchMedicalRecordResult response = new SearchMedicalRecordResult(medicalRecordDtos);
        return new DataResult<>(true, "Successfully fetched data!",response);
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
        String uri = className == PersonDTO[].class ? fetchPeopleUri : fetchDiseasesUri;
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

    private List<MedicalRecord> createMedicalRecords(List<DiseaseDTO> diseaseDTOs, List<PersonDTO> peopleDTOs){
        return peopleDTOs.stream().map(p -> {
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
