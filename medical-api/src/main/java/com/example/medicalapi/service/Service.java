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


    public void findByUserId(){
        JSONObject obj = new JSONObject();
        obj.put("userId",Integer.valueOf(1));
        obj.put("firstName","Jack");
        obj.put("lastName","Jackson");
        obj.put("weight", Float.valueOf(76.3f));
        obj.put("age",Integer.valueOf(37));
        obj.put("diseases", List.of("disease1, disease2, disease3"));
        System.out.println(obj.toJSONString());
        MedicalRecord mr = jsonToPojo(obj);
        System.out.println(mr);
    }



    public MedicalRecord jsonToPojo(JSONObject jsonObject){
        ObjectMapper mapper = new ObjectMapper().registerModule(new JsonOrgModule());
        return mapper.convertValue(jsonObject, MedicalRecord.class);
    }
}
