package com.example.medicalapi.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DiseaseModel {
    private int id;
    private String name;
    private boolean curable;
}
