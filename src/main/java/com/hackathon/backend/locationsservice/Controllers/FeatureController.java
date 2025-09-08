package com.hackathon.backend.locationsservice.Controllers;

import com.hackathon.backend.locationsservice.Controllers.RequestDTO.FeatureDTO;
import com.hackathon.backend.locationsservice.Domain.Feature;
import com.hackathon.backend.locationsservice.Domain.LocationScope.Location;
import com.hackathon.backend.locationsservice.Services.FeatureService;
import com.hackathon.backend.locationsservice.Services.LocationScope.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/locations/{locationId}/features")
@RequiredArgsConstructor
public class FeatureController {
    private final FeatureService featureService;
    private final LocationService locationService;

    @GetMapping("")
    public ResponseEntity<?> getFeatures(@PathVariable(name = "locationId") UUID locationId) {
        Optional<Location> location = locationService.getById(locationId);
        if (location.isPresent()) {
            List<Feature> features = featureService.getAllFeaturesByLocationId(locationId);
            return ResponseEntity.ok(features);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "error", "not_found",
                    "message", "Локацію не знайдено"
            ));
        }

    }

    @PostMapping("")
    public ResponseEntity<?> addFeature(@PathVariable(name = "locationId") UUID locationId,
                                        @RequestBody FeatureDTO featureDTO) {
        Optional<Location> location = locationService.getById(locationId);
        if (location.isPresent()) {
            Feature feature = featureService.addFeature(locationId, featureDTO);
            return new ResponseEntity<>(feature, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "error", "not_found",
                    "message", "Локацію не знайдено"
            ));
        }
    }

    @PutMapping("/{featureId}")
    public ResponseEntity<?> updateFeature(@PathVariable(name = "locationId") UUID locationId,
                                           @PathVariable(name = "featureId") UUID featureId,
                                           @RequestBody FeatureDTO featureDTO) {
        Optional<Location> location = locationService.getById(locationId);
        if (location.isPresent()) {
            Optional<Feature> feature = featureService.getById(featureId);
            if (feature.isPresent()) {
                //TODO QualityRating validation

                featureService.updateFeature(feature.get(), featureDTO);
                return ResponseEntity.ok(feature);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "error", "not_found",
                        "message", "Елемент безбар'єрності не знайдено"
                ));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "error", "not_found",
                    "message", "Локацію не знайдено"
            ));
        }
    }

    @DeleteMapping("/{featureId}")
    public ResponseEntity<?> deleteFeatureById(@PathVariable(name = "locationId") UUID locationId,
                                               @PathVariable(name = "featureId") UUID featureId) {
        Optional<Location> location = locationService.getById(locationId);
        if (location.isPresent()) {
            Optional<Feature> feature = featureService.getById(featureId);
            if (feature.isPresent()) {
                featureService.deleteFeature(featureId);
                return ResponseEntity.ok(Map.of("message", "Елемент безбар'єрності успішно видалено"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "error", "not_found",
                        "message", "Елемент безбар'єрності не знайдено"
                ));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "error", "not_found",
                    "message", "Локацію не знайдено"
            ));
        }
    }


}
