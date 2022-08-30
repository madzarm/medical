package com.example.medicalapi.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalRecordDto {
    private String firstName;
    private String lastName;
    private float weight;
    private int age;
    private List<DiseaseHistoryDto> diseaseHistories;
}
