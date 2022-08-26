package com.example.medicalapi.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PersonDTO {
    private int userid;
    private String name;
    private String surname;
    private int weight;
    private int age;
}
