package com.demo.dummyapi.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import com.demo.dummyapi.entity.Person;

@Service
public class RandomPersonService{

	List<String> NAMES = List.of("Maksim", "Marija", "Martina", "Zlatko", "Sara", "Tin", "Dominik", "Dragica", "Josip", "Tomislav", "Marko", "Mario", "Vladimir", "Ana", "Dorotea");

	List<String> SURNAMES = List.of("Horvat", "Borkovic", "Kanceljak", "Madzar", "Safar", "Cvetkovic", "Beljan", "Grgec", "Ciglenecki", "Katovcic", "Jagunic", "Braje", "Vitkovic");


	public List<Person> generatePeople() {
		List<Person> people = new ArrayList<>();
		for(int i = 0; i<10000; i++) {
			var name = NAMES.get(ThreadLocalRandom.current().nextInt(0, NAMES.size()));
			var surname = SURNAMES.get(ThreadLocalRandom.current().nextInt(0, SURNAMES.size()));
			var weight = ThreadLocalRandom.current().nextInt(40, 140);
			var age = ThreadLocalRandom.current().nextInt(14, 90);


			var result = new Person(name, surname, weight, age);
			result.setName(name);
			result.setSurname(surname);
			result.setWeight(weight);
			result.setAge(age);

			people.add(result);
		}
		System.out.println(people.size());
		return people;

	}

}
