package com.example.medicalapi.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class DiseaseDTO {
    private int userid;
    private String diseases;
}
