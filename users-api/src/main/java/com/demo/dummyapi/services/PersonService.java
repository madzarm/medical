package com.demo.dummyapi.services;

import com.demo.dummyapi.entity.Person;
import com.demo.dummyapi.repository.DiseaseHistoryRepository;
import com.demo.dummyapi.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final DiseaseHistoryRepository diseaseHistoryRepository;

    public PersonService(PersonRepository personRepository, DiseaseHistoryRepository diseaseHistoryRepository) {
        this.personRepository = personRepository;
        this.diseaseHistoryRepository = diseaseHistoryRepository;
    }


    public List<Person> getPeople(){
        return personRepository.findAll();
    }

    public Person getPerson(int id) {
        return personRepository.findById((long) id).orElse(null);
    }
}
