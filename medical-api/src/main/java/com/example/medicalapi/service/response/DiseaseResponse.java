package com.example.medicalapi.service.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiseaseResponse {
    private int id;
    private String name;
    private boolean curable;
}
