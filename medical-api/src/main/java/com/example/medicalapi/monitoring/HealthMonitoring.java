package com.example.medicalapi.monitoring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;
import org.json.simple.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
@EnableScheduling
public class HealthMonitoring {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final RestTemplateBuilder restTemplateBuilder;

    public HealthMonitoring(KafkaTemplate<String, String> kafkaTemplate, RestTemplateBuilder restTemplateBuilder) {
        this.kafkaTemplate = kafkaTemplate;
        this.restTemplateBuilder = restTemplateBuilder;
    }

    @Scheduled(fixedRate = 300000)
    public void printSomething(){
        String url = "http://localhost:8080/api/v1/actuator/health";

        String topic = "medical-api.health";

        RestTemplate restTemplate = restTemplateBuilder.basicAuthentication("username","password").build();
        JSONObject response = restTemplate.getForObject(url, JSONObject.class);
        kafkaTemplate.send(topic, String.valueOf(response));
    }



}
