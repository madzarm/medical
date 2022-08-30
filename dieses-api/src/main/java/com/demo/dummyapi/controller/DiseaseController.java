package com.demo.dummyapi.controller;

import java.util.List;

import com.demo.dummyapi.entity.Disease;
import com.demo.dummyapi.services.DiseaseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping(value = "/api")
@RestController
public class DiseaseController {

	private final DiseaseService diseaseService;

	public DiseaseController(DiseaseService diseaseService) {
		this.diseaseService = diseaseService;
	}

	@GetMapping("/diseases")
	public List<Disease> getAllDiseases() {
		return diseaseService.getAllDiseases();
	}

}
