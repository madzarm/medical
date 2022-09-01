package com.example.medicalapi.service.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDiseaseHistoryRequest {
    @NotNull
    private int userId;
    @NotNull
    private List<Integer> diseaseIds;
}
