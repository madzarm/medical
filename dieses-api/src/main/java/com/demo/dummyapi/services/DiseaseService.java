package com.demo.dummyapi.services;

import com.demo.dummyapi.entity.Disease;
import com.demo.dummyapi.repository.DiseaseRepository;
import com.demo.dummyapi.services.request.CreateDiseaseRequest;
import com.demo.dummyapi.services.result.ActionResult;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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

    public Disease getDiseaseById(int id){
        return diseaseRepository.findById((long) id).orElse(null);
    }

    public List<Disease> getDiseasesByName(String name){
        return diseaseRepository.getDiseaseByNameStartingWith(name);
    }

    public List<Disease> getDiseasesByIds(Integer[] ids){
        List<Long> longIds = Arrays.stream(ids).map(i -> (long) i).toList();
        return diseaseRepository.getDiseaseByIdIn(longIds);
    }

    public ActionResult createDisease(CreateDiseaseRequest request){
        Disease disease = new Disease(request.getDiseaseName(),request.isCurable());
        diseaseRepository.save(disease);
        return new ActionResult(true, "Disease successfully created!");
    }
}
