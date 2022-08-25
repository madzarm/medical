package com.demo.dummyapi.entity;


import javax.persistence.*;

@Entity
public class Person {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
    private int userid;
	private String name;

	private String surname;

	private int weight;

	private int age;

	public Person() {

	}

	public Person(String name, String surname, int weight, int age) {
		super();
		this.name = name;
		this.surname = surname;
		this.weight = weight;
		this.age = age;
	}

	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}


	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public int getWeight() {
		return weight;
	}

	public int getAge() {
		return age;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public void setAge(int age) {
		this.age = age;
	}

}
