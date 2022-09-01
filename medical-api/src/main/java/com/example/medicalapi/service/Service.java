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
import com.example.medicalapi.service.body.GetPeopleByDiseaseHistoryDate;
import com.example.medicalapi.service.body.GetPeopleByIdsBody;
import com.example.medicalapi.service.request.GetPeopleByNameRequest;
import com.example.medicalapi.service.result.DataResult;
import com.example.medicalapi.service.result.SearchMedicalRecordResult;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class Service {

    @Value("${fetch.people.endpoint.uri}")
    private String fetchAllPeopleUri;
    @Value("${fetch.people.by-disease-ids.endpoint.uri}")
    private String fetchPeopleByDiseaseIdsUri;
    @Value("${fetch.people.by-name.endpoint.uri}")
    private String fetchPeopleByNameUri;
    @Value("${fetch.people.by-date.endpoint.uri}")
    private String fetchPeopleByDateUri;
    @Value("${fetch.person.by-id.endpoint.uri}")
    private String fetchPersonByIdUri;
    @Value("${fetch.diseases.endpoint.uri}")
    private String fetchAllDiseasesUri;
    @Value("${api.diseases.name}")
    private String diseaseApiName;
    @Value("${api.users.name}")
    private String usersApiName;

    public DataResult<SearchMedicalRecordResult> findAll() throws ExecutionException, InterruptedException {
        CompletableFuture<PersonModel[]> personModels = fetchAllPeopleData();
        CompletableFuture<DiseaseModel[]> diseaseModels = fetchAllDiseaseData();
        CompletableFuture.allOf(diseaseModels, personModels).join();

        List<PersonModel> personModelsList = Arrays.asList(personModels.get());
        List<DiseaseModel> diseaseModelsList = Arrays.asList(diseaseModels.get());

        if (personModelsList.isEmpty())
            throw new EmptyResponseException(usersApiName);
        else if (diseaseModelsList.isEmpty())
            throw new EmptyResponseException(diseaseApiName);
        return getResult(personModelsList, diseaseModelsList);

    }
    public DataResult<SearchMedicalRecordResult> findByPersonId(int id) throws ExecutionException, InterruptedException {
        CompletableFuture<PersonModel> personModel = fetchPersonById(id);
        CompletableFuture<DiseaseModel[]> diseaseModels = fetchAllDiseaseData();
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
        CompletableFuture<DiseaseModel[]> diseaseModels = fetchAllDiseaseData();
        List<DiseaseModel> diseaseModelsList = Arrays.asList(diseaseModels.get());
        int[] ids = {id};
        CompletableFuture<PersonModel[]> personModels = fetchPeopleByDiseaseIds(ids);
        List<PersonModel> personModelsList = Arrays.asList(personModels.get());

        if (personModelsList.isEmpty())
            throw new EmptyResponseException(usersApiName);
        else if (diseaseModelsList.isEmpty())
            throw new EmptyResponseException(diseaseApiName);
        return getResult(personModelsList,diseaseModelsList);
    }

    public DataResult<SearchMedicalRecordResult> findByDiseaseName(String name) throws ExecutionException, InterruptedException {
        CompletableFuture<DiseaseModel[]> diseaseModels = fetchAllDiseaseData();
        List<DiseaseModel> diseaseModelsList = Arrays.asList(diseaseModels.get());
        int[] ids = diseaseModelsList.stream().filter(d -> d.getName().contains(name)).map(DiseaseModel::getId).mapToInt(d -> d).toArray();
        CompletableFuture<PersonModel[]> personModels = fetchPeopleByDiseaseIds(ids);
        List<PersonModel> personModelsList = Arrays.asList(personModels.get());


        if (personModelsList.isEmpty())
            throw new EmptyResponseException(usersApiName);
        else if (diseaseModelsList.isEmpty())
            throw new EmptyResponseException(diseaseApiName);

        return getResult(personModelsList,diseaseModelsList);
    }
    public DataResult<SearchMedicalRecordResult> findByName(String firstName, String lastName) throws ExecutionException, InterruptedException {
        CompletableFuture<DiseaseModel[]> diseaseModels = fetchAllDiseaseData();
        CompletableFuture<PersonModel[]> personModels = fetchPeopleByName(firstName,lastName);
        CompletableFuture.allOf(diseaseModels,personModels).join();

        List<DiseaseModel> diseaseModelList = Arrays.asList(diseaseModels.get());
        List<PersonModel> personModelList = Arrays.asList(personModels.get());
        return getResult(personModelList,diseaseModelList);
    }
    public DataResult<SearchMedicalRecordResult> findByDate(LocalDate from, LocalDate to) throws ExecutionException, InterruptedException {
        CompletableFuture<DiseaseModel[]> diseaseModels = fetchAllDiseaseData();
        CompletableFuture<PersonModel[]> personModels = fetchPeopleByDiseaseHistoryDate(from,to);
        CompletableFuture.allOf(diseaseModels,personModels).join();

        List<DiseaseModel> diseaseModelList = Arrays.asList(diseaseModels.get());
        List<PersonModel> personModelList = Arrays.asList(personModels.get());
        return getResult(personModelList,diseaseModelList);
    }
    @Async("taskExecutor")
    public CompletableFuture<DiseaseModel[]> fetchAllDiseaseData(){
        RestTemplate restTemplate = new RestTemplate();
        DiseaseModel[] response;
        try {
            response = restTemplate.getForObject(fetchAllDiseasesUri, DiseaseModel[].class);
        } catch (Exception e) {
            throw new DiseasesApiConnectionRefusedException();
        }
        return CompletableFuture.completedFuture(response);
    }
    @Async("taskExecutor")
    public CompletableFuture<PersonModel[]> fetchAllPeopleData() {
        RestTemplate restTemplate = new RestTemplate();
        PersonModel[] response;
        try {
            System.out.println("HOW MANY TIMES");
            response = restTemplate.getForObject(fetchAllPeopleUri, PersonModel[].class);
        } catch (Exception e) {
            throw new UsersApiConnectionRefusedException();
        }
        return CompletableFuture.completedFuture(response);
    }

    @Async("taskExecutor")
    public CompletableFuture<PersonModel> fetchPersonById(int id){
        RestTemplate restTemplate = new RestTemplate();
        PersonModel response;
        String uri = fetchPersonByIdUri + id;
        response = restTemplate.getForObject(uri, PersonModel.class);

        return CompletableFuture.completedFuture(response);
    }
    @Async("taskExecutor")
    public CompletableFuture<PersonModel[]> fetchPeopleByDiseaseIds(int[] ids){
        RestTemplate restTemplate = new RestTemplate();
        PersonModel[] response;
        GetPeopleByIdsBody body = new GetPeopleByIdsBody(Arrays.stream(ids).boxed().toArray(Integer[]::new));
        response = restTemplate.postForObject(fetchPeopleByDiseaseIdsUri,body,PersonModel[].class);

        return CompletableFuture.completedFuture(response);
    }
    @Async("taskExecutor")
    public CompletableFuture<PersonModel[]> fetchPeopleByName(String firstName, String lastName){
        RestTemplate restTemplate = new RestTemplate();
        PersonModel[] response;
        GetPeopleByNameRequest body = new GetPeopleByNameRequest(firstName,lastName);
        response = restTemplate.postForObject(fetchPeopleByNameUri,body,PersonModel[].class);
        return CompletableFuture.completedFuture(response);
    }

    @Async("taskExecutor")
    public CompletableFuture<PersonModel[]> fetchPeopleByDiseaseHistoryDate(LocalDate from, LocalDate to) {
        RestTemplate restTemplate = new RestTemplate();
        PersonModel[] response;
        GetPeopleByDiseaseHistoryDate body = new GetPeopleByDiseaseHistoryDate(from,to);
        response = restTemplate.postForObject(fetchPeopleByDateUri,body, PersonModel[].class);
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
