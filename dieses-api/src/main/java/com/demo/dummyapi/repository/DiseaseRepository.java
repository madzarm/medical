package com.demo.dummyapi.repository;

import com.demo.dummyapi.entity.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiseaseRepository extends JpaRepository<Disease,Long> {
    List<Disease> getDiseaseByIdIn(List<Long> ids);
    List<Disease> getDiseaseByNameStartingWith(String name);
}
