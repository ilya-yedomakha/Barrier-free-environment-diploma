package com.hackathon.backend.locationsservice.Controllers.BarrierlessCriteria;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.BarrierlessCriteriaScope.BarrierlessCriteriaGroupCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.BarrierlessCriteriaScope.BarrierlessCriteriaTypeCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.BarrierlessCriteriaScope.BarrierlessCriteriaGroupReadDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.BarrierlessCriteriaScope.BarrierlessCriteriaTypeReadDTO;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaGroup;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaType;
import com.hackathon.backend.locationsservice.Result.Result;
import com.hackathon.backend.locationsservice.Services.BarrierlessCriteriaScope.BarrierlessCriteriaGroupService;
import com.hackathon.backend.locationsservice.Services.BarrierlessCriteriaScope.BarrierlessCriteriaTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/barrierless_criteria_type")
@RequiredArgsConstructor
public class BarrierlessCriteriaTypeController {
    private final BarrierlessCriteriaTypeService barrierlessCriteriaTypeService;
    @GetMapping("/")
    public ResponseEntity<?> getAllBarrierlessCriteriaTypes() {
        Result<BarrierlessCriteriaType, BarrierlessCriteriaTypeReadDTO> Result = barrierlessCriteriaTypeService.getAll();
        if (Result.isSuccess()) {
            return ResponseEntity.ok(Result.getEntityDTOs());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Result.getError());
        }
    }

    @GetMapping("/{barrierless_criteria_type_id}/")
    public ResponseEntity<?> getBarrierlessCriteriaTypeById(@PathVariable(name = "barrierless_criteria_type_id") UUID barrierlessCriteriaTypeId) {
        Result<BarrierlessCriteriaType, BarrierlessCriteriaTypeReadDTO> Result = barrierlessCriteriaTypeService.getById(barrierlessCriteriaTypeId);
        if (Result.isSuccess()) {
            return ResponseEntity.ok(Result.getEntityDTO());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Result.getError());
        }
    }

    @PostMapping
    ResponseEntity<?> add(@RequestBody BarrierlessCriteriaTypeCreateDTO barrierlessCriteriaTypeCreateDTO) {
        Result<BarrierlessCriteriaType, BarrierlessCriteriaTypeReadDTO> Result = barrierlessCriteriaTypeService.add(barrierlessCriteriaTypeCreateDTO);
        if (Result.isSuccess()) {
            return ResponseEntity.ok(Result.getEntityDTO());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.getError());
        }
    }
}
