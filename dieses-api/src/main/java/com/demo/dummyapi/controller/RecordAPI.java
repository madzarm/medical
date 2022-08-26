package com.demo.dummyapi.controller;

import java.util.List;

import com.demo.dummyapi.repository.Repository;
import com.demo.dummyapi.services.RandomRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.dummyapi.entity.Record;

@RequestMapping(value = "/api")
@RestController
public class RecordAPI {

	private final RandomRecordService recordService;
	private final Repository repository;

	@Autowired
	public RecordAPI(RandomRecordService recordService, Repository repository){
		this.recordService = recordService;
		this.repository = repository;
	}

	@GetMapping(value = "/random")
	public void randomRecords() {

		List<Record> result = recordService.generateRecord();

		repository.saveAll(result);
	}
	@GetMapping("/records")
	public List<Record> getRecords() {
		return repository.findAll();
	}

}
