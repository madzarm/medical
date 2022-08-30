package com.demo.dummyapi.config;

import com.demo.dummyapi.entity.Disease;
import com.demo.dummyapi.repository.DiseaseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(1)
public class DatabaseConfig implements CommandLineRunner {

    private final DiseaseRepository diseaseRepository;

    public DatabaseConfig(DiseaseRepository diseaseRepository) {
        this.diseaseRepository = diseaseRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Disease disease1 = new Disease("disease1",false);
        Disease disease2 = new Disease("disease2",true);
        Disease disease3 = new Disease("disease3",false);
        diseaseRepository.saveAll(List.of(disease1,disease2,disease3));
    }
}
