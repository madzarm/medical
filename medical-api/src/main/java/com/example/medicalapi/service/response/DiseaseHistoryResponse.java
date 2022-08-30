package com.example.medicalapi.service.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiseaseHistoryResponse {
    private int id;
    private LocalDate dateDiscovered;
    private int diseaseId;
}
