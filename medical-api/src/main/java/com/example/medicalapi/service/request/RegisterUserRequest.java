package com.example.medicalapi.service.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequest {

    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String role;
}
