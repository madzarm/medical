package com.demo.dummyapi.controller;

import java.util.List;

import com.demo.dummyapi.entity.Disease;
import com.demo.dummyapi.services.DiseaseService;
import com.demo.dummyapi.services.GetDiseasesRequest;
import org.springframework.web.bind.annotation.*;


@RequestMapping(value = "/api")
@RestController
public class DiseaseController {

	private final DiseaseService diseaseService;

	public DiseaseController(DiseaseService diseaseService) {
		this.diseaseService = diseaseService;
	}

	@GetMapping("/diseases")
	public List<Disease> getDiseases() {
		return diseaseService.getAllDiseases();
	}

	@GetMapping(value ="/disease/{id}")
	public Disease getDiseases(@PathVariable("id") int id) {
		return diseaseService.getDiseaseById(id);
	}

	@PostMapping("/diseases")
	public List<Disease> getDiseases(@RequestBody GetDiseasesRequest request) {
		return diseaseService.getDiseasesByIds(request.getIds());
	}

}
