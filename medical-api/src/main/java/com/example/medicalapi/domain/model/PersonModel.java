package com.example.medicalapi.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonModel {
    private int id;
    private String name;
    private String surname;
    private int weight;
    private int age;
    private List<DiseaseHistoryModel> diseaseHistories = new ArrayList<>();
}
