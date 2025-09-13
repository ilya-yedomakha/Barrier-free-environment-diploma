package com.hackathon.backend.locationsservice.Controllers;

import com.hackathon.backend.locationsservice.DTOs.VerificationDTO;
import com.hackathon.backend.locationsservice.Services.LocationScope.LocationService;
import com.hackathon.backend.locationsservice.Services.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
/* // provide verification result pattern
    @GetMapping("/{locationId}/verifications")
    public ResponseEntity<?> getVerifications(@PathVariable(name = "locationId") UUID locationId) {
        Result<Location, LocationReadDTO> result = locationService.getById(locationId);
        if (result.isSuccess()) {
            if (result.entity != null) {
                Result<Verification,VerificationDTO> verification = verificationService.getAllVerificationsById(locationId);
                return ResponseEntity.ok(verification);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "error", "not_found",
                        "message", "Локацію не знайдено"
                ));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result.getError());
        }

    }*/

}
