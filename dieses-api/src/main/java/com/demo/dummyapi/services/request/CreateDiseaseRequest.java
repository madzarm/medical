package com.demo.dummyapi.services.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDiseaseRequest {
    @NotBlank
    private String diseaseName;
    @NotNull
    private boolean curable;
}
