package com.demo.dummyapi.services.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPeopleByDiseaseHistoryDate {
    private LocalDate from;
    private LocalDate to;
}
