package com.hackathon.backend.locationsservice.Controllers.LocationScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.LocationScope.LocationCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.LocationScope.LocationTypeCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.LocationScope.LocationReadDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.LocationScope.LocationTypeReadDTO;
import com.hackathon.backend.locationsservice.DTOs.RecordDTOs.LocationScope.LocationTypeWithGroupDTO;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.Location;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.LocationType;
import com.hackathon.backend.locationsservice.Result.Result;
import com.hackathon.backend.locationsservice.Services.LocationScope.LocationTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/location-types")
@RequiredArgsConstructor

public class LocationTypeController {

    private final LocationTypeService locationTypeService;

    @GetMapping("/")
    public ResponseEntity<?> getAllLocationTypes() {
        Result<LocationType, LocationTypeReadDTO> Result = locationTypeService.getAll();
        if (Result.isSuccess()) {
            return ResponseEntity.ok(Result.getEntityDTOs());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Result.getError());
        }
    }

    @GetMapping("/{location_type_id}/")
    public ResponseEntity<?> getLocationById(@PathVariable(name = "location_type_id") UUID locationTypeId) {
        Result<LocationType, LocationTypeReadDTO> Result = locationTypeService.getById(locationTypeId);
        if (Result.isSuccess()) {
            return ResponseEntity.ok(Result.getEntityDTO());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Result.getError());
        }
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<?> add(@RequestBody LocationTypeCreateDTO locationTypeCreateDTO) {
        Result<LocationType, LocationTypeReadDTO> Result = locationTypeService.add(locationTypeCreateDTO);
        if (Result.isSuccess()) {
            return ResponseEntity.ok(Result.getEntityDTO());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.getError());
        }
    }

    @GetMapping("/{id}/criteria-tree")
    ResponseEntity<?> getCriteriaTree(@PathVariable UUID id) {
        Result<LocationType, LocationTypeWithGroupDTO> Result = locationTypeService.getCriteriaTree(id);
        if (Result.isSuccess()) {
            return ResponseEntity.ok(Result.getEntityDTO());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.getError());
        }
    }
}
