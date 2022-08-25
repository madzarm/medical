package com.demo.dummyapi.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import com.demo.dummyapi.entity.Person;

@Service
public class RandomPersonService{

	List<String> NAMES = List.of("Acquired immunodeficiency syndrome (AIDS)", "Chlamydia infection", "COVID-19", "Coxsackievirus", "Ebola haemorrhagic fever", "Epidemic louse-borne typhus", "Gonorrhoea", "Hepatitis", "Human papillomavirus infection", "Invasive Haemophilus influenzae disease", "Louse-borne typhus", "Malaria", "Measles", "Pneumococcal disease", "Rabies", "Smallpox", "Syphilis", "Tetanus", "Tuberculosis", "Mononucleosis", "Zika virus disease");


	public List<Person> generatePeople() {
		List<Person> people = new ArrayList<>();
		for(int i = 0; i<1000; i++) {
			List diseases = new ArrayList();
			String name;
			for(int j = 0; j<(ThreadLocalRandom.current().nextInt(0,5)); j++) {
				name = NAMES.get(ThreadLocalRandom.current().nextInt(0, NAMES.size()));
				diseases.add(name);
			}
			String listString = String.join(", ", diseases);
			var result = new Person(listString);
			result.setDiseases(listString);

			people.add(result);
		}
		return people;

	}

}
