package com.example.medicalapi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "logstash-mr")
public class MedicalRecord {
    private int userId;
    private String firstName;
    private String lastName;
    private float weight;
    private int age;
    private List<String> diseases;
}
