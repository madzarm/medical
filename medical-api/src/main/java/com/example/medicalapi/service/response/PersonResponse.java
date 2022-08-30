package com.example.medicalapi.service.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonResponse {
    private int id;
    private String name;
    private String surname;
    private int weight;
    private int age;
    private List<DiseaseHistoryResponse> diseaseHistories = new ArrayList<>();
}
