package com.hackathon.backend.locationsservice.Controllers.BarrierlessCriteria;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.BarrierlessCriteriaScope.BarrierlessCriteriaGroupCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.LocationScope.LocationCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.BarrierlessCriteriaScope.BarrierlessCriteriaGroupReadDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.BarrierlessCriteriaScope.BarrierlessCriteriaReadDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.LocationScope.LocationReadDTO;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteria;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaGroup;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.Location;
import com.hackathon.backend.locationsservice.Result.Result;
import com.hackathon.backend.locationsservice.Services.BarrierlessCriteriaScope.BarrierlessCriteriaGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/barrierless_criteria_group")
@RequiredArgsConstructor
public class BarrierlessCriteriaGroupController {
    private final BarrierlessCriteriaGroupService barrierlessCriteriaGroupService;

    @GetMapping("/")
    public ResponseEntity<?> getAllBarrierlessCriteriaGroups() {
        Result<BarrierlessCriteriaGroup, BarrierlessCriteriaGroupReadDTO> Result = barrierlessCriteriaGroupService.getAll();
        if (Result.isSuccess()) {
            return ResponseEntity.ok(Result.getEntityDTOs());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Result.getError());
        }
    }

    @GetMapping("/{barrierless_criteria_group_id}/")
    public ResponseEntity<?> getBarrierlessCriteriaGroupById(@PathVariable(name = "barrierless_criteria_group_id") UUID barrierlessCriteriaGroupId) {
        Result<BarrierlessCriteriaGroup, BarrierlessCriteriaGroupReadDTO> Result = barrierlessCriteriaGroupService.getById(barrierlessCriteriaGroupId);
        if (Result.isSuccess()) {
            return ResponseEntity.ok(Result.getEntityDTO());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Result.getError());
        }
    }

    @PostMapping
    ResponseEntity<?> add(@RequestBody BarrierlessCriteriaGroupCreateDTO barrierlessCriteriaGroupCreateDTO) {
        Result<BarrierlessCriteriaGroup, BarrierlessCriteriaGroupReadDTO> Result = barrierlessCriteriaGroupService.add(barrierlessCriteriaGroupCreateDTO);
        if (Result.isSuccess()) {
            return ResponseEntity.ok(Result.getEntityDTO());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.getError());
        }
    }
}
