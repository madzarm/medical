package com.example.medicalapi.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DiseaseHistoryDto {
    private int id;
    private LocalDate dateDiscovered;
    private String diseaseName;
}
