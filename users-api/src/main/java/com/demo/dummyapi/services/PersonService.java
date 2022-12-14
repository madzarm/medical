package com.demo.dummyapi.services;

import com.demo.dummyapi.entity.DiseaseHistory;
import com.demo.dummyapi.entity.Person;
import com.demo.dummyapi.repository.DiseaseHistoryRepository;
import com.demo.dummyapi.repository.PersonRepository;
import com.demo.dummyapi.services.request.*;
import com.demo.dummyapi.services.result.ActionResult;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final DiseaseHistoryRepository diseaseHistoryRepository;
    public PersonService(PersonRepository personRepository, DiseaseHistoryRepository diseaseHistoryRepository) {
        this.personRepository = personRepository;
        this.diseaseHistoryRepository = diseaseHistoryRepository;
    }

    public List<Person> getPeopleByDiseaseId(int id){
        return personRepository.findAllByDiseaseHistoriesId((long)id);
    }
    public List<Person> getPeople(){
        return personRepository.findAll();
    }

    public Person getPerson(int id) {
        return personRepository.findById((long) id).orElse(null);
    }

    public List<Person> getPeopleByDiseaseIds(GetPeopleByDiseaseIdsRequest request) {
        List<Integer> ints = Arrays.asList(request.getIds());
        List<Long> longs = ints.stream().mapToLong(d -> d).boxed().collect(Collectors.toList());
        return personRepository.findAllByDiseaseHistoriesDiseaseIdIn(longs);
    }

    public List<Person> getPeopleByName(GetPeopleByNameRequest request) {
        boolean hasFirstName = !request.getFirstName().isBlank();
        boolean hasLastName = !request.getLastName().isBlank();
        if(hasFirstName && hasLastName)
            return personRepository.findAllByNameAndSurname(request.getFirstName(),request.getLastName());
        else if(hasFirstName)
            return personRepository.findAllByNameStartingWith(request.getFirstName());
        return personRepository.findAllBySurnameStartingWith(request.getLastName());
    }

    public List<Person> getPeopleByDate(GetPeopleByDiseaseHistoryDate request) {
        LocalDate from = request.getFrom();
        LocalDate to = request.getTo();
        boolean hasFrom = from != null;
        boolean hasTo = to != null;
        if(hasFrom && hasTo)
            return personRepository.findAllDistinctByDiseaseHistoriesDateDiscoveredBetween(from,to);
        else if (hasFrom)
            return personRepository.findAllDistinctByDiseaseHistoriesDateDiscoveredAfter(from);
        return personRepository.findAllDistinctByDiseaseHistoriesDateDiscoveredBefore(to);
    }

    public ActionResult createMedicalRecord(CreateMedicalRecordRequst request) {
        List<DiseaseHistory> diseaseHistories = request.getDiseaseIds().stream()
                .map(id -> DiseaseHistory.builder()
                        .dateDiscovered(LocalDate.now())
                        .diseaseId((long)id).build()
                ).collect(Collectors.toList());
        Person person = Person.builder()
                .age(request.getAge())
                .weight(request.getWeight())
                .surname(request.getLastName())
                .name(request.getFirstName()).build();
        person.setDiseaseHistories(diseaseHistories);

        personRepository.save(person);
        diseaseHistoryRepository.saveAll(diseaseHistories);

        return new ActionResult(true, "Successfully created a medical record!");
    }
    public ActionResult createDiseaseHistory(CreateDiseaseHistoryRequest request){
        List<DiseaseHistory> diseaseHistories = request.getDiseaseIds().stream()
                .map(id -> DiseaseHistory.builder()
                        .dateDiscovered(LocalDate.now())
                        .diseaseId((long)id).build()
                ).collect(Collectors.toList());
        Person person = personRepository.findById((long) request.getUserId()).get();

        person.addDiseaseHistories(diseaseHistories);

        personRepository.save(person);
        diseaseHistoryRepository.saveAll(diseaseHistories);

        return new ActionResult(true, "Successfully created a disease history!");
    }

}
