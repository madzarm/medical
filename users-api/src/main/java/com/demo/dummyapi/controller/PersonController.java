package com.demo.dummyapi.controller;

import com.demo.dummyapi.entity.Person;
import com.demo.dummyapi.services.PersonService;
import com.demo.dummyapi.services.request.GetPeopleByDiseaseIdsRequest;
import com.demo.dummyapi.services.request.GetPeopleByNameRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

	@PostMapping("/people")
	public List<Person> getPeopleByDiseaseIds(@RequestBody GetPeopleByDiseaseIdsRequest request) {
		return personService.getPeopleByDiseaseIds(request);
	}
	@PostMapping("/people-by-name")
	public List<Person> getPeopleByName(@Valid @RequestBody GetPeopleByNameRequest request) {
		return personService.getPeopleByName(request);
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