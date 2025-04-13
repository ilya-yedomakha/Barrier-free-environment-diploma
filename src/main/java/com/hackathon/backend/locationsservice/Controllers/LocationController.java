package com.hackathon.backend.locationsservice.Controllers;

import com.hackathon.backend.locationsservice.Controllers.RequestDTO.VerificationDTO;
import com.hackathon.backend.locationsservice.Domain.Enums.LocationStatusEnum;
import com.hackathon.backend.locationsservice.Domain.Location;
import com.hackathon.backend.locationsservice.Domain.helper.filters.LocationFilter;
import com.hackathon.backend.locationsservice.Services.LocationService;
import com.hackathon.backend.locationsservice.Services.ServiceLocation;
import com.hackathon.backend.locationsservice.exceptions.ValidationFilterException;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;
    private final ServiceLocation serviceLocation;

    @GetMapping()
    public ResponseEntity<?> getLocations(
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lng,
            @RequestParam(required = false) Integer radius,
            @RequestParam(required = false) String types,
            @RequestParam(required = false) String features,
            @RequestParam(required = false) @Min(1) @Max(5) Integer minScore,
            @RequestParam(required = false) LocationStatusEnum status,
            @RequestParam(required = false) Boolean verified,
            @RequestParam(required = false) String query,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "20") @Min(1) @Max(100) Integer limit
    ){
        Map<String, Object> filters = new HashMap<>();

        if (lat != null) filters.put("lat", lat);
        if (lng != null) filters.put("lng", lng);
        if (radius != null) filters.put("radius", radius);
        if (types != null && !types.isBlank()) filters.put("types", types);
        if (features != null && !features.isBlank()) filters.put("features", features);
        if (minScore != null) filters.put("minScore", minScore);
        if (status != null) filters.put("status", status);
        if (verified != null) filters.put("verified", verified);
        if (query != null && !query.isBlank()) filters.put("query", query);
        filters.put("page", page);
        filters.put("limit", limit);

        List<Location> locations = serviceLocation.dynamicSearch(filters);
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{ocation_id}/")
    public ResponseEntity<?> getLocationById(@PathVariable(name = "ocation_id") UUID locationId) {
        Optional<Location> location = locationService.getById(locationId);
        if (location.isPresent()) {
            return ResponseEntity.ok(location.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Location with ID " + locationId + " was not found.");
        }
    }

    @PostMapping
    ResponseEntity<?> save(@RequestBody Location location){
        Location newLocation = locationService.add(location);
        return ResponseEntity.ok(newLocation);
    }


}
