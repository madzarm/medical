package com.example.medicalapi.service;

import com.example.medicalapi.domain.MedicalRecord;
import com.example.medicalapi.service.result.DataResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;
import org.json.simple.JSONObject;

import javax.json.JsonObject;
import java.util.List;

@org.springframework.stereotype.Service
public class Service {

    public MedicalRecord jsonToPojo(JSONObject jsonObject){
        ObjectMapper mapper = new ObjectMapper().registerModule(new JsonOrgModule());
        return mapper.convertValue(jsonObject, MedicalRecord.class);
    }
}
