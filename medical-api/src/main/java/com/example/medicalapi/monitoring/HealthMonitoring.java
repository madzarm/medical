package com.example.medicalapi.monitoring;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
@EnableScheduling
public class HealthMonitoring {

    @Value("${fetch.health.endpoint.uri}")
    private String uri;
    @Value("${kafka.topic.health}")
    private String topic;
    @Value("${basic.auth.username}")
    private String username;
    @Value("${basic.auth.password}")
    private String password;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final RestTemplateBuilder restTemplateBuilder;

    public HealthMonitoring(KafkaTemplate<String, String> kafkaTemplate, RestTemplateBuilder restTemplateBuilder) {
        this.kafkaTemplate = kafkaTemplate;
        this.restTemplateBuilder = restTemplateBuilder;
    }

    @Scheduled(fixedRate = 300000)
    public void printSomething(){
        RestTemplate restTemplate = restTemplateBuilder.basicAuthentication(username,password).build();
        JSONObject response = restTemplate.getForObject(uri, JSONObject.class);
        kafkaTemplate.send(topic, String.valueOf(response));
    }



}
