package com.demo.dummyapi.controller;

import java.util.List;
import java.util.Optional;

import com.demo.dummyapi.repository.Repository;
import com.demo.dummyapi.services.RandomPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.dummyapi.entity.Person;
import org.springframework.web.client.RestTemplate;

@RequestMapping(value = "/api")
@RestController
public class PersonController {

	private final RandomPersonService personService;
	private final Repository repository;
	private final RestTemplate restTemplate;

	@Autowired
	public PersonController(RandomPersonService personService, Repository repository, RestTemplate restTemplate){
		this.personService = personService;
		this.repository = repository;
		this.restTemplate = restTemplate;
	}

	@GetMapping(value = "/random")
	public void randomPeople() {

		List<Person> result = personService.generatePeople();

		System.out.println(result);

		repository.saveAll(result);
	}
	@GetMapping("/people")
	public List<Person> getPeople() {
		return repository.findAll();
	}

	@GetMapping(value = "/person/{id}")
	public Optional<Person> getRecords(@PathVariable("id") Integer Id) {
		return repository.findById(Id);
	}
}
