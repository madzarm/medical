package com.example.medicalapi.service.result;

import com.example.medicalapi.domain.dto.DiseaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchDiseasesResult {
    private List<DiseaseDto> diseases;
}
