package com.example.medicalapi.service.result;

import com.example.medicalapi.domain.PersonDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchPersonResult {
    private List<PersonDTO> people;
}
