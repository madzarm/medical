package com.demo.dummyapi.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
public class Person {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
    private Long id;
	private String name;
	private String surname;
	private int weight;
	private int age;
	@OneToMany(mappedBy = "person")
	private List<DiseaseHistory> diseaseHistories = new ArrayList<>();

	public Person(String name, String surname, int weight, int age, List<DiseaseHistory> diseaseHistories) {
		this.name = name;
		this.surname = surname;
		this.weight = weight;
		this.age = age;
		this.diseaseHistories = diseaseHistories;
	}

	public Person(String name, String surname, int weight, int age) {
		this.name = name;
		this.surname = surname;
		this.weight = weight;
		this.age = age;
	}
}
