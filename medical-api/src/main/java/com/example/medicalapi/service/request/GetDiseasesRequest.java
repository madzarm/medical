package com.example.medicalapi.service.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class GetDiseasesRequest {
    int[] ids;
}
