package com.demo.dummyapi.services.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPeopleByDiseaseIdsRequest {
    private Integer[] ids;
}
