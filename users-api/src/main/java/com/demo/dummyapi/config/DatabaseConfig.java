package com.demo.dummyapi.config;

import com.demo.dummyapi.entity.DiseaseHistory;
import com.demo.dummyapi.entity.Person;
import com.demo.dummyapi.repository.DiseaseHistoryRepository;
import com.demo.dummyapi.repository.PersonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Order(1)
public class DatabaseConfig implements CommandLineRunner {

    private final DiseaseHistoryRepository diseaseHistoryRepository;
    private final PersonRepository personRepository;

    public DatabaseConfig(DiseaseHistoryRepository diseaseHistoryRepository, PersonRepository personRepository) {
        this.diseaseHistoryRepository = diseaseHistoryRepository;
        this.personRepository = personRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Person person1 = new Person("Jack","Jackson",10,16);
        Person person2 = new Person("Jack","Jackson",10,16);
        Person person3 = new Person("Jack","Jackson",10,16);
        personRepository.saveAll(List.of(person2,person1,person3));
        DiseaseHistory diseaseHistory1 = new DiseaseHistory(person1, LocalDate.now(),1L);
        DiseaseHistory diseaseHistory2 = new DiseaseHistory(person1, LocalDate.now(),2L);
        DiseaseHistory diseaseHistory3 = new DiseaseHistory(person3, LocalDate.now(),2L);
        diseaseHistoryRepository.saveAll(List.of(diseaseHistory1,diseaseHistory2,diseaseHistory3));
        person1.setDiseaseHistories(List.of(diseaseHistory1,diseaseHistory2));
        person3.setDiseaseHistories(List.of(diseaseHistory3));


        personRepository.saveAll(List.of(person2,person1,person3));
        diseaseHistoryRepository.saveAll(List.of(diseaseHistory1,diseaseHistory2,diseaseHistory3));

    }
}
