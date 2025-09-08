package com.hackathon.backend.locationsservice.Controllers;

import com.hackathon.backend.locationsservice.Controllers.RequestDTO.VerificationDTO;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.Location;
import com.hackathon.backend.locationsservice.Domain.Verification;
import com.hackathon.backend.locationsservice.Services.LocationScope.LocationService;
import com.hackathon.backend.locationsservice.Services.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationService verificationService;
    private final LocationService locationService;

    @PostMapping("/{locationId}/verifications")
    ResponseEntity addVerification(@PathVariable(name = "locationId") UUID locationId,
                                   @RequestBody VerificationDTO requestBody) {

//        Verification new_verification = verificationService.add(requestBody);
        return ResponseEntity.ok(true);

    }

    @GetMapping("/{locationId}/verifications")
    public ResponseEntity<?> getVerifications(@PathVariable(name = "locationId") UUID locationId) {
        Optional<Location> location = locationService.getById(locationId);
        if (location.isPresent()) {
            List<Verification> verification = verificationService.getAllVerificationsById(locationId);
            return ResponseEntity.ok(verification);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "error", "not_found",
                    "message", "Локацію не знайдено"
            ));
        }
    }

}
