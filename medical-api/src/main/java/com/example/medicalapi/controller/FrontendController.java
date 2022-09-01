package com.example.medicalapi.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class FrontendController {

    @GetMapping("/api-docs/users")
    private @ResponseBody String getusers()
    {
        final String uri = "http://localhost:8081/api-docs";
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(uri, String.class);
    }

    @GetMapping("/api-docs/diseases")
    private @ResponseBody String getdiseases()
    {
        final String uri = "http://localhost:8082/api-docs";
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(uri, String.class);
    }

    @GetMapping("/home")
    @HystrixCommand(fallbackMethod = "fallback_hello", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")
    })
    public String showindex(Model model) throws InterruptedException {
        model.addAttribute("users", "test");
        Thread.sleep(30000);
        return "index";
    }
    private String fallback_hello(Model model) {
        return "toolong";
    }
    @ModelAttribute("users")
    public String test(){
        return "Test";
    }
}