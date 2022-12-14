package com.demo.dummyapi.controller;

import java.util.List;

import com.demo.dummyapi.entity.Disease;
import com.demo.dummyapi.services.DiseaseService;
import com.demo.dummyapi.services.GetDiseasesRequest;
import com.demo.dummyapi.services.request.CreateDiseaseRequest;
import com.demo.dummyapi.services.result.ActionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


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

	@GetMapping(value ="/disease/name/{name}")
	public List<Disease> getDiseases(@PathVariable("name") String name) {
		return diseaseService.getDiseasesByName(name);
	}

	@PostMapping("/diseases")
	public List<Disease> getDiseases(@RequestBody GetDiseasesRequest request) {
		return diseaseService.getDiseasesByIds(request.getIds());
	}

	@PostMapping("/disease")
	public ResponseEntity<ActionResult> createDisease(@Valid @RequestBody CreateDiseaseRequest request) {
		return diseaseService.createDisease(request).intoResponseEntity();
	}

}
