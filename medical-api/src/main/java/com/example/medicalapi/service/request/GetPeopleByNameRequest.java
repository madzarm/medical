package com.example.medicalapi.service.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class GetPeopleByNameRequest {
    private String firstName="";
    private String lastName="";

    public GetPeopleByNameRequest(String firstName, String lastName) {
        if(firstName!=null)
            this.firstName = firstName;
        if(lastName!=null)
            this.lastName = lastName;
    }
}
