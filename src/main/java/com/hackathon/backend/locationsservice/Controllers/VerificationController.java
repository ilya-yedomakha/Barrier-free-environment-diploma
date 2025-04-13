package com.hackathon.backend.locationsservice.Controllers;

import com.hackathon.backend.locationsservice.Controllers.RequestDTO.VerificationDTO;
import com.hackathon.backend.locationsservice.Domain.Verification;
import com.hackathon.backend.locationsservice.Services.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationService verificationService;

    @PostMapping("/{locationId}/verifications")
    ResponseEntity addVerification(@PathVariable(name = "locationId") UUID locationId,
                                   @RequestBody VerificationDTO requestBody){

//        Verification new_verification = verificationService.add(requestBody);
        return ResponseEntity.ok(true);

    }

}
