package com.example.medicalapi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DiseaseHistory {
    private int id;
    private String diseaseName;
    private LocalDate dateDiscovered;
    private boolean curable;
}
