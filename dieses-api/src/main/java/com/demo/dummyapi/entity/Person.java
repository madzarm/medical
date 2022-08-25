package com.demo.dummyapi.entity;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Person {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
    private int userid;
	private String diseases;

	public Person() {

	}

	public Person(String diseases) {
		super();
		this.diseases = diseases;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getDiseases() {
		return diseases;
	}

	public void setDiseases(String diseases) {
		this.diseases = diseases;
	}


}
