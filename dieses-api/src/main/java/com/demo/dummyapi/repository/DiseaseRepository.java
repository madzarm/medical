package com.demo.dummyapi.repository;

import com.demo.dummyapi.entity.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiseaseRepository extends JpaRepository<Disease,Long> {
}
