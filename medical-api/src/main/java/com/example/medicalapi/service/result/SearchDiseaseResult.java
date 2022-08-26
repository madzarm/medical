package com.example.medicalapi.service.result;

import com.example.medicalapi.domain.DiseaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchDiseaseResult {
    private List<DiseaseDTO> diseases;
}
