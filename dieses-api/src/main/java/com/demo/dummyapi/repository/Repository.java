package com.demo.dummyapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.dummyapi.entity.Record;

import java.util.Optional;


@org.springframework.stereotype.Repository
public interface Repository extends JpaRepository<Record, Integer> {
    Optional<Record> findById(Integer id);
}
