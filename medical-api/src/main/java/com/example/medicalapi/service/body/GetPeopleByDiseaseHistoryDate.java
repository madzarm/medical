package com.example.medicalapi.service.body;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetPeopleByDiseaseHistoryDate {
    private LocalDate from;
    private LocalDate to;
}
