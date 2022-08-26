package com.example.medicalapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MedicalApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedicalApiApplication.class, args);
    }

}
