package com.example.medicalapi.domain;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class DiseaseHistory {
    private int id;
    private String diseaseName;
    private LocalDate dateDiscovered;
    private boolean curable;
}
