package com.demo.dummyapi.services;

import com.demo.dummyapi.entity.Disease;
import com.demo.dummyapi.repository.DiseaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiseaseService {

    private final DiseaseRepository diseaseRepository;

    public DiseaseService(DiseaseRepository diseaseRepository) {
        this.diseaseRepository = diseaseRepository;
    }

    public List<Disease> getAllDiseases(){
        return diseaseRepository.findAll();
    }
}
