package com.hackathon.backend.locationsservice.Controllers.BarrierlessCriteria;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.BarrierlessCriteriaScope.BarrierlessCriteriaCheckCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.BarrierlessCriteriaScope.BarrierlessCriteriaCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.BarrierlessCriteriaScope.BarrierlessCriteriaCheckReadDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.BarrierlessCriteriaScope.BarrierlessCriteriaReadDTO;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteria;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaCheck;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaCheckEmbeddedId;
import com.hackathon.backend.locationsservice.Result.Result;
import com.hackathon.backend.locationsservice.Services.BarrierlessCriteriaScope.BarrierlessCriteriaCheckService;
import com.hackathon.backend.locationsservice.exceptions.NotFoundEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/barrier-criteria-check")
@AllArgsConstructor
public class BarrierlessCriteriaCheckController {

    private BarrierlessCriteriaCheckService barrierlessCriteriaCheckService;

    @GetMapping("/location/{location_id}/barrierless_criteria/{barrierless_criteria_id}/user/{user_id}")
    BarrierlessCriteriaCheck getBarrierlessCriteriaCheck(@PathVariable("location_id") String locationId,
                                                         @PathVariable("barrierless_criteria_id") String barrierlessCriteriaId,
                                                         @PathVariable("user_id") String userId) {

        return barrierlessCriteriaCheckService.findById(UUID.fromString(locationId),
                UUID.fromString(barrierlessCriteriaId),
                UUID.fromString(userId)).orElseThrow(
                () -> new NotFoundEntity("Barrierless Criteria Not Found")
        );
    }

    @PostMapping
    ResponseEntity<?> save(@RequestBody BarrierlessCriteriaCheckCreateDTO check) {

        Result<BarrierlessCriteriaCheck, BarrierlessCriteriaCheckReadDTO> Result = barrierlessCriteriaCheckService.add(check);

        if (Result.isSuccess()) {
            return ResponseEntity.ok(Result.getEntityDTO());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.getError());
        }
    }

//    @PutMapping
//    ResponseEntity<BarrierlessCriteriaCheck> updateBarrierlessCriteriaCheck(@RequestBody BarrierlessCriteriaCheck check) {
//        return ResponseEntity.ok(barrierlessCriteriaCheckService.save(check));
//    }

    @GetMapping("/location/{location_id}")
    ResponseEntity<List<BarrierlessCriteriaCheckReadDTO>> getBarrierlessCriteriaCheckByLocation(@PathVariable("location_id") String id) {

        return ResponseEntity.ok(barrierlessCriteriaCheckService.findAllBarrierCriteriaCheckByLocationId(UUID.fromString(id)));
    }

    @GetMapping("/location/{location_id}/criteria/{criteria_id}")
    ResponseEntity<List<BarrierlessCriteriaCheck>> getBarrierlessCriteriaCheckByLocationAndCriteria(@PathVariable("location_id") String locationId,
                                                                                                    @PathVariable("criteria_id") String criteriaId) {

        return ResponseEntity.ok(barrierlessCriteriaCheckService.findAllBarrierCriteriaCheckByLocationIdAndCriteria(UUID.fromString(locationId), UUID.fromString(criteriaId)));
    }

    @GetMapping("/user/{user_id}")
    ResponseEntity<List<BarrierlessCriteriaCheckReadDTO>> getBarrierlessCriteriaCheckByUser(@PathVariable("user_id") String id) {

        return ResponseEntity.ok(barrierlessCriteriaCheckService.findAllBarrierCriteriaCheckByUserId(UUID.fromString(id)));
    }
}
