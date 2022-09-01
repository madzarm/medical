package com.demo.dummyapi.services.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMedicalRecordRequst {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Min(0)
    @Max(500)
    private int weight;
    @Min(0)
    @Max(150)
    private int age;
    private List<Integer> diseaseIds = new ArrayList<>();
}
