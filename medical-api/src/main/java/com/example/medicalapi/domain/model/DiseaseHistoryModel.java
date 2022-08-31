package com.example.medicalapi.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiseaseHistoryModel {
    private int id;
    private LocalDate dateDiscovered;
    private int diseaseId;
}
