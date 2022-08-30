package com.demo.dummyapi.repository;

import com.demo.dummyapi.entity.DiseaseHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiseaseHistoryRepository extends JpaRepository<DiseaseHistory,Long> {
}
