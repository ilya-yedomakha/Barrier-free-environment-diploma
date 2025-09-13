package com.hackathon.backend.locationsservice.Controllers.BarrierlessCriteria;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.BarrierlessCriteriaScope.BarrierlessCriteriaReadDTO;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteria;
import com.hackathon.backend.locationsservice.Result.Result;
import com.hackathon.backend.locationsservice.Services.BarrierlessCriteriaScope.BarrierlessCriteriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/barrierless_criteria")
@RequiredArgsConstructor
public class BarrierlessCriteriaController {

    private final BarrierlessCriteriaService barrierlessCriteriaService;

    @GetMapping("/")
    public ResponseEntity<?> getAllBarrierlessCriterias() {
        Result<BarrierlessCriteria, BarrierlessCriteriaReadDTO> Result = barrierlessCriteriaService.getAll();
        if (Result.isSuccess()) {
            return ResponseEntity.ok(Result.getEntityDTOs());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Result.getError());
        }
    }

    @GetMapping("/{barrierless_criteria_id}/")
    public ResponseEntity<?> getBarrierlessCriteriaById(@PathVariable(name = "barrierless_criteria_id") UUID barrierlessCriteriaId) {
        Result<BarrierlessCriteria, BarrierlessCriteriaReadDTO> Result = barrierlessCriteriaService.getById(barrierlessCriteriaId);
        if (Result.isSuccess()) {
            return ResponseEntity.ok(Result.getEntityDTO());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Result.getError());
        }
    }
}
