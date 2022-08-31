package com.example.medicalapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Controller
public class FrontendController {

    @GetMapping("/api-docs/users")
    private @ResponseBody String getusers()
    {
        final String uri = "http://localhost:8081/api-docs";

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);

        return result;
    }

    @GetMapping("/api-docs/diseases")
    private @ResponseBody String getdiseases()
    {
        final String uri = "http://localhost:8082/api-docs";

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);

        return result;
    }
}