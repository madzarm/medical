package com.demo.dummyapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.demo.dummyapi.entity.Record;


@org.springframework.stereotype.Repository
public interface Repository extends JpaRepository<Record, Integer> {
    Optional<Record> findById(Integer id);

}
