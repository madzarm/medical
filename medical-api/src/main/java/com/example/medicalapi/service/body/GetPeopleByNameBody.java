package com.example.medicalapi.service.body;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class GetPeopleByNameBody {
    private String firstName="";
    private String lastName="";

    public GetPeopleByNameBody(String firstName, String lastName) {
        if(firstName!=null)
            this.firstName = firstName;
        if(lastName!=null)
            this.lastName = lastName;
    }
}
