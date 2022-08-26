package com.demo.dummyapi.controller;

import java.util.List;
import java.util.Optional;

import com.demo.dummyapi.repository.Repository;
import com.demo.dummyapi.services.RandomRecordService;
import net.bytebuddy.dynamic.scaffold.TypeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.dummyapi.entity.Record;

@RequestMapping(value = "/api")
@RestController
public class RecordController {

	private final RandomRecordService recordService;
	private final Repository repository;

	@Autowired
	public RecordController(RandomRecordService recordService, Repository repository){
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

	@GetMapping(value = "/record/{id}")
	public Optional<Record> getRecords(@PathVariable("id") Integer Id) {
		return repository.findById(Id);
	}
}
