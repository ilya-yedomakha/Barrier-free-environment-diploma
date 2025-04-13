package com.hackathon.backend.locationsservice.Controllers;
import com.hackathon.backend.locationsservice.Controllers.RequestDTO.FeatureDTO;
import com.hackathon.backend.locationsservice.Domain.Feature;
import com.hackathon.backend.locationsservice.Domain.Location;
import com.hackathon.backend.locationsservice.Services.FeatureService;
import com.hackathon.backend.locationsservice.Services.LocationService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("{locationId}/features")
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
                    "message", "Локацію не знайдено",
                    "details", null
            ));
        }

    }

    @PostMapping("")
    public ResponseEntity<?> addFeature(@PathVariable(name = "locationId") UUID locationId,
                                        @RequestBody FeatureDTO featureDTO){
        Optional<Location> location = locationService.getById(locationId);
        if (location.isPresent()) {
            Feature feature = featureService.addFeature(locationId, featureDTO);
            return new ResponseEntity<>(feature, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "error", "not_found",
                    "message", "Локацію не знайдено",
                    "details", null
            ));
        }
    }

//    @PutMapping("/{featureId}")
//    public ResponseEntity<?> updateFeature(@PathVariable UUID locationId,
//                                           @PathVariable UUID featureId,
//                                           @RequestBody FeatureDTO featureDTO) {
//        if (locationService.findById(locationId) == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
//                    "error", "not_found",
//                    "message", "Локацію не знайдено",
//                    "details", null
//            ));
//        }
//
//        FeatureDTO updatedFeature = featureService.updateFeature(locationId, featureId, featureDTO);
//        return ResponseEntity.ok(updatedFeature);
//    }
//
//    @DeleteMapping("/{featureId}")
//    public ResponseEntity<?> deleteFeatureById(@PathVariable UUID locationId,
//                                           @PathVariable UUID featureId){
//
//        featureService.deleteFeature(locationId, featureId);
//        return ResponseEntity.ok(Map.of("message", "Елемент безбар'єрності успішно видалено"));
//    }
}
