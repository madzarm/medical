package com.example.medicalapi.service;

import com.example.medicalapi.domain.DiseaseHistory;
import com.example.medicalapi.domain.MedicalRecord;
import com.example.medicalapi.domain.dto.DiseaseHistoryDto;
import com.example.medicalapi.domain.dto.MedicalRecordDto;
import com.example.medicalapi.domain.model.DiseaseHistoryModel;
import com.example.medicalapi.domain.model.DiseaseModel;
import com.example.medicalapi.domain.model.PersonModel;
import com.example.medicalapi.exception.exceptions.DiseasesApiConnectionRefusedException;
import com.example.medicalapi.exception.exceptions.EmptyResponseException;
import com.example.medicalapi.exception.exceptions.UsersApiConnectionRefusedException;
import com.example.medicalapi.service.result.DataResult;
import com.example.medicalapi.service.result.SearchMedicalRecordResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class Service {

    @Value("${fetch.people.endpoint.uri}")
    private String fetchPeopleUri;
    @Value("${fetch.person.endpoint.uri}")
    private String fetchPersonUri;
    @Value("${fetch.person.by-disease.endpoint.uri}")
    private String fetchPersonByDiseaseUri;
    @Value("${fetch.diseases.endpoint.uri}")
    private String fetchDiseasesUri;
    @Value("${fetch.disease.endpoint.uri}")
    private String fetchDiseaseUri;
    @Value("${api.diseases.name}")
    private String diseaseApiName;
    @Value("${api.users.name}")
    private String usersApiName;


    public DataResult<SearchMedicalRecordResult> findAll() throws ExecutionException, InterruptedException {
        CompletableFuture<PersonModel[]> personModels = fetchData(PersonModel[].class);
        CompletableFuture<DiseaseModel[]> diseaseModels = fetchData(DiseaseModel[].class);
        CompletableFuture.allOf(diseaseModels,personModels).join();

        List<PersonModel> personModelsList = Arrays.asList(personModels.get());
        List<DiseaseModel> diseaseModelsList = Arrays.asList(diseaseModels.get());

        if(personModelsList.isEmpty())
           throw new EmptyResponseException(usersApiName);
       else if (diseaseModelsList.isEmpty())
           throw new EmptyResponseException(diseaseApiName);
        return getResult(personModelsList, diseaseModelsList);

    }

    public DataResult<SearchMedicalRecordResult> findByPersonId(int id) throws ExecutionException, InterruptedException {
        CompletableFuture<PersonModel> personModel = fetchById(PersonModel.class,id);
        CompletableFuture<DiseaseModel[]> diseaseModels = fetchData(DiseaseModel[].class);
        CompletableFuture.allOf(personModel,diseaseModels).join();

        List<PersonModel> personModelsList = Collections.singletonList(personModel.get());
        List<DiseaseModel> diseaseModelsList = Arrays.asList(diseaseModels.get());

        if (personModelsList.get(0)==null)
            throw new EmptyResponseException(usersApiName);
        else if (diseaseModelsList.isEmpty())
            throw new EmptyResponseException(diseaseApiName);
        return getResult(personModelsList, diseaseModelsList);
    }
    public DataResult<SearchMedicalRecordResult> findByDiseaseId(int id) throws ExecutionException, InterruptedException {
        CompletableFuture<DiseaseModel[]> diseaseModels = fetchData(DiseaseModel[].class);
        List<DiseaseModel> diseaseModelsList = Arrays.asList(diseaseModels.get());
        CompletableFuture<PersonModel[]> personModels = fetchById(PersonModel[].class,id);
        List<PersonModel> personModelsList = Arrays.asList(personModels.get());

        if (personModelsList.isEmpty())
            throw new EmptyResponseException(usersApiName);
        else if (diseaseModelsList.get(0)==null)
            throw new EmptyResponseException(diseaseApiName);
        return getResult(personModelsList,diseaseModelsList);
    }

    @Async("taskExecutor")
    public <T> CompletableFuture<T> fetchById(Class<T> className, int id){
        RestTemplate restTemplate = new RestTemplate();
        System.out.println(className);
        String uri = (className == PersonModel.class ? fetchPersonUri :
                (className == PersonModel[].class ? fetchPersonByDiseaseUri : fetchDiseaseUri)) + id;
        T response;
        System.out.println(uri);
        try {
            response = restTemplate.getForObject(uri, className);
        }
        catch (Exception e){
            if(className == PersonModel.class)
                throw new UsersApiConnectionRefusedException();
            throw new DiseasesApiConnectionRefusedException();
        }
        return CompletableFuture.completedFuture(response);
    }
//    @Async("taskExecutor")
//    public List<DiseaseModel> fetchDiseasesById(List<Integer> ids){
//        RestTemplate restTemplate = new RestTemplate();
//        String uri = "http://localhost:8082/api/diseases";
//        int[] intIds = ids.stream().mapToInt(Integer::intValue).toArray();
//        GetDiseasesRequest getDiseasesRequest = new GetDiseasesRequest(intIds);
//        DiseaseModel[] diseaseModels = restTemplate.postForObject(uri, getDiseasesRequest,DiseaseModel[].class);
//        if(diseaseModels!=null)
//            return Arrays.stream(diseaseModels).collect(Collectors.toList());
//        return new ArrayList<>();
//    }

    @Async("taskExecutor")
    public <T> CompletableFuture<T> fetchData(Class<T> className){
        RestTemplate restTemplate = new RestTemplate();
        String uri = className == PersonModel[].class ? fetchPeopleUri : fetchDiseasesUri;
        T response;
        try {
            response = restTemplate.getForObject(uri, className);
        } catch (Exception e) {
           if(className == PersonModel[].class)
               throw new UsersApiConnectionRefusedException();
           throw new DiseasesApiConnectionRefusedException();
        }
        return CompletableFuture.completedFuture(response);
    }
    private DataResult<SearchMedicalRecordResult> getResult(List<PersonModel> personModelsList, List<DiseaseModel> diseaseModelsList) {
        List<MedicalRecordDto> medicalRecordDtos = createMedicalRecordDtos(personModelsList,diseaseModelsList);

        SearchMedicalRecordResult searchMedicalRecordResult = new SearchMedicalRecordResult();
        searchMedicalRecordResult.setMedicalRecords(medicalRecordDtos);

        return new DataResult<>(true, "Successfully fetched data!", searchMedicalRecordResult);
    }

    private List<MedicalRecordDto> createMedicalRecordDtos(List<PersonModel> personModels, List<DiseaseModel> diseaseModels){
        return personModels.stream().map(p -> {
            List<DiseaseHistory> diseaseHistories = modelToDiseaseHistory(p.getDiseaseHistories(),diseaseModels);

            return MedicalRecordDto.builder()
                    .diseaseHistories(mapDiseaseHistoryToDto(diseaseHistories))
                    .firstName(p.getName())
                    .lastName(p.getSurname())
                    .age(p.getAge())
                    .weight(p.getWeight()).build();
        }).collect(Collectors.toList());
    }

    private List<MedicalRecord> createMedicalRecords(List<PersonModel> personModels, List<DiseaseModel> diseaseModels){
        return personModels.stream().map(p -> {

            List<DiseaseHistory> diseaseHistories = modelToDiseaseHistory(p.getDiseaseHistories(),diseaseModels);

            return MedicalRecord.builder()
                    .diseaseHistories(diseaseHistories)
                    .userId(p.getId())
                    .age(p.getAge())
                    .weight(p.getWeight())
                    .firstName(p.getName())
                    .lastName(p.getSurname()).build();
        }).collect(Collectors.toList());
    }

    private List<DiseaseHistory> modelToDiseaseHistory
            (List<DiseaseHistoryModel> diseaseHistoryModels, List<DiseaseModel> diseaseModels){
        return diseaseHistoryModels.stream().map(dh -> {
            Optional<DiseaseModel> diseaseOptional = diseaseModels.stream().filter(dr -> dr.getId() == dh.getDiseaseId()).findFirst();
            return diseaseOptional.map(response -> DiseaseHistory.builder()
                    .diseaseName(response.getName())
                    .id(dh.getId())
                    .curable(response.isCurable())
                    .dateDiscovered(dh.getDateDiscovered()).build()).orElse(null);
        }).collect(Collectors.toList());
    }

    private List<MedicalRecordDto> mapMedicalRecordToDto(List<MedicalRecord> medicalRecords){
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
                    .curable(dh.isCurable()).build()
         ).collect(Collectors.toList());
    }
}
