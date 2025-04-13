package com.hackathon.backend.locationsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.hackathon.backend.locationsservice")
public class LocationsServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(LocationsServiceApplication.class, args);
    }
}
