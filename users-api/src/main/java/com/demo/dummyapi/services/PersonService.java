package com.demo.dummyapi.services;

import com.demo.dummyapi.entity.Person;
import com.demo.dummyapi.repository.PersonRepository;
import com.demo.dummyapi.services.request.GetPeopleByDiseaseHistoryDate;
import com.demo.dummyapi.services.request.GetPeopleByDiseaseIdsRequest;
import com.demo.dummyapi.services.request.GetPeopleByNameRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
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
            return personRepository.findAllByName(request.getFirstName());
        return personRepository.findAllBySurname(request.getLastName());
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
}
