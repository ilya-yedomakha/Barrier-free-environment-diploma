package com.hackathon.backend.locationsservice.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class WelcomeController {

    @GetMapping
    public ResponseEntity<?> welcome() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to Locations Service API");
        response.put("status", "running");
        response.put("endpoints", new String[]{
                "/api/locations",
                "/api/locations/{location_id}"
        });
        return ResponseEntity.ok(response);
    }
}
