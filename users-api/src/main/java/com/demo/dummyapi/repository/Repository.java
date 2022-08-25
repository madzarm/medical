package com.demo.dummyapi.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.dummyapi.entity.Person;


@org.springframework.stereotype.Repository
public interface Repository extends JpaRepository<Person, Integer> {

		Optional<Person> findByUserid(Integer userid);

}
