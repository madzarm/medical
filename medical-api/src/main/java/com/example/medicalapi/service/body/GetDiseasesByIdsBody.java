package com.example.medicalapi.service.body;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetDiseasesByIdsBody {
    private Integer[] ids;
}
