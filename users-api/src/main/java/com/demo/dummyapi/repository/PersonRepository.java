package com.demo.dummyapi.repository;

import com.demo.dummyapi.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person,Long> {

    List<Person> findAllByDiseaseHistoriesId(Long id);
    List<Person> findAllByDiseaseHistoriesDiseaseIdIn(List<Long> ids);
    List<Person> findAllByNameAndSurname(String name, String surname);
    List<Person> findAllByNameStartingWith(String name);
    List<Person> findAllBySurnameStartingWith(String name);
    List<Person> findAllDistinctByDiseaseHistoriesDateDiscoveredBetween(LocalDate from, LocalDate to);
    List<Person> findAllDistinctByDiseaseHistoriesDateDiscoveredBefore(LocalDate to);
    List<Person> findAllDistinctByDiseaseHistoriesDateDiscoveredAfter(LocalDate from);
}
