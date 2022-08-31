package com.demo.dummyapi.services;

import com.demo.dummyapi.entity.Disease;
import com.demo.dummyapi.repository.DiseaseRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiseaseService {

    private final DiseaseRepository diseaseRepository;

    public DiseaseService(DiseaseRepository diseaseRepository) {
        this.diseaseRepository = diseaseRepository;
    }

    public List<Disease> getAllDiseases(){
        return diseaseRepository.findAll();
    }

    public List<Disease> getDiseasesByIds(Integer[] ids){
        List<Long> longIds = Arrays.stream(ids).map(i -> (long) i).toList();
        return diseaseRepository.getDiseaseByIdIn(longIds);
    }
}
