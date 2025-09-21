package com.hackathon.backend.locationsservice.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class DemoController {

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {

        return ResponseEntity.ok("Hello User");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> helloA() {

        return ResponseEntity.ok("Hello Admin");
    }
}
