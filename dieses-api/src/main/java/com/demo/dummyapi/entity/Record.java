package com.demo.dummyapi.entity;


import javax.persistence.*;

@Entity
public class Record {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
    private int userid;
	private String diseases;

	public Record() {

	}

	public Record(String diseases) {
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

	@Override
	public String toString() {
		return "Record{" +
				"userid=" + userid +
				", diseases='" + diseases + '\'' +
				'}';
	}
}
