package com.demo.dummyapi.controller;

import com.demo.dummyapi.entity.Person;
import com.demo.dummyapi.services.PersonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequestMapping(value = "/api")
@RestController
public class PersonController {

	private final PersonService personService;

	public PersonController(PersonService personService) {
		this.personService = personService;
	}
	@GetMapping("/people")
	public List<Person> getPeople() {
		return personService.getPeople();
	}

	@GetMapping(value = "/person/{id}")
	public Person getPerson(@PathVariable("id") int id) {
		return personService.getPerson(id);
	}
	@GetMapping(value = "/person/disease/{id}")
	public List<Person> getPeopleByDiseaseId(@PathVariable("id") int id){
		return personService.getPeopleByDiseaseId(id);
	}
}
