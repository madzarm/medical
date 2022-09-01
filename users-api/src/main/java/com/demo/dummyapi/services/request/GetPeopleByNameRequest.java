package com.demo.dummyapi.services.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPeopleByNameRequest {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
}
