package com.hackathon.backend.locationsservice.Controllers.LocationScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.LocationScope.LocationCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.LocationScope.LocationReadDTO;
import com.hackathon.backend.locationsservice.DTOs.ViewLists.LocationListViewDTO;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.Location;
import com.hackathon.backend.locationsservice.Domain.Enums.LocationStatusEnum;
import com.hackathon.backend.locationsservice.Result.Result;
import com.hackathon.backend.locationsservice.Services.LocationScope.LocationService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping()
    public ResponseEntity<?> getLocations(
            @RequestParam(name = "lat", required = false) Double lat,
            @RequestParam(name = "lng", required = false) Double lng,
            @RequestParam(name = "radius", required = false) Integer radius,
            @RequestParam(name = "types", required = false) String types,
            @RequestParam(name = "features", required = false) String features,
            @RequestParam(name = "minScore", required = false) @Min(1) @Max(5) Integer minScore,
            @RequestParam(name = "status", required = false) LocationStatusEnum status,
            @RequestParam(name = "verified", required = false) Boolean verified,
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "page", required = false) @Max(100) Integer page

    ) {
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



        Result<Location,LocationListViewDTO> result = locationService.getAll(filters);

        return ResponseEntity.ok(result.getEntityDTO());
    }

    @GetMapping("/{location_id}/")
    public ResponseEntity<?> getLocationById(@PathVariable(name = "location_id") UUID locationId) {
        Result<Location, LocationReadDTO> Result = locationService.getById(locationId);
        if (Result.isSuccess()) {
            return ResponseEntity.ok(Result.getEntityDTO());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Result.getError());
        }
    }

//    @GetMapping("/{location_id}/barrierless_criteria_checks")
//    public ResponseEntity<?> getBarrierlessCriteriaChecksByLocationId(@PathVariable(name = "location_id") UUID locationId) {
//        Result<Location, LocationReadDTO> Result = locationService.getById(locationId);
//        if (Result.isSuccess()) {
//            return ResponseEntity.ok(Result.getEntityDTO());
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Result.getError());
//        }
//    }
    @PutMapping("/merge/{new_location_id}/into/{old_location_id}/")
    public ResponseEntity<?> mergeBarrierLocationChecks(@PathVariable(name = "new_location_id") UUID newLocationId, @PathVariable(name = "old_location_id") UUID oldLocationId){
        Result<Location, LocationReadDTO> Result = locationService.mergeBarrierLocationChecks(newLocationId, oldLocationId);
        if (Result.isSuccess()) {
            return ResponseEntity.ok(Result.getEntityDTO());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Result.getError());
        }
    }

//    @PutMapping("/{location_id}/")

    @PostMapping
    ResponseEntity<?> add(@RequestBody LocationCreateDTO locationCreateDTO) {
        Result<Location, LocationReadDTO> Result = locationService.add(locationCreateDTO);
        if (Result.isSuccess()) {
            return ResponseEntity.ok(Result.getEntityDTO());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.getError());
        }
    }

    @GetMapping("/test")
    public ResponseEntity<?> testEndpoint() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "LocationController is working correctly");
        return ResponseEntity.ok(response);
    }
}
