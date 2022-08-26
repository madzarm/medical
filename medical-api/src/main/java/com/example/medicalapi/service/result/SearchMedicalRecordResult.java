package com.example.medicalapi.service.result;

import com.example.medicalapi.domain.dto.MedicalRecordDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchMedicalRecordResult {
    private List<MedicalRecordDto> medicalRecords;
}
