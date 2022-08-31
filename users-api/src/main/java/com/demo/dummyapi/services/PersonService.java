package com.demo.dummyapi.services;

import com.demo.dummyapi.entity.Person;
import com.demo.dummyapi.repository.DiseaseHistoryRepository;
import com.demo.dummyapi.repository.PersonRepository;
import com.demo.dummyapi.services.request.GetPeopleByDiseaseIdsRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
        return personRepository.findAllByDiseaseHistoriesIdIn(longs);
    }
}
