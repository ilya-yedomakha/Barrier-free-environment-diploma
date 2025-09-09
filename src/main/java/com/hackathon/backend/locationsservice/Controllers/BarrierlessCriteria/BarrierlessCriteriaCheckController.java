package com.hackathon.backend.locationsservice.Controllers.BarrierlessCriteria;

import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteria;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaCheck;
import com.hackathon.backend.locationsservice.Services.BarrierlessCriteriaScope.BarrierlessCriteriaCheckService;
import com.hackathon.backend.locationsservice.exceptions.NotFoundEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/barrier-criteria-check")
@AllArgsConstructor
public class BarrierlessCriteriaCheckController {

    private BarrierlessCriteriaCheckService barrierlessCriteriaCheckService;

    @GetMapping("/{id}")
    BarrierlessCriteriaCheck getBarrierlessCriteriaCheck(@PathVariable("id") String id) {

       return barrierlessCriteriaCheckService.findById(UUID.fromString(id)).orElseThrow(
                ()-> new NotFoundEntity("Barrierless Criteria Not Found")
        );
    }

    @PostMapping
    ResponseEntity<BarrierlessCriteriaCheck> createBarrierlessCriteriaCheck(@RequestBody BarrierlessCriteriaCheck check) {
        return ResponseEntity.ok(barrierlessCriteriaCheckService.save(check));
    }

    @PutMapping
    ResponseEntity<BarrierlessCriteriaCheck> updateBarrierlessCriteriaCheck(@RequestBody BarrierlessCriteriaCheck check) {
        return ResponseEntity.ok(barrierlessCriteriaCheckService.save(check));
    }

    @GetMapping("/location/{location_id}")
    ResponseEntity<List<BarrierlessCriteriaCheck>> getBarrierlessCriteriaCheckByLocation(@PathVariable("location_id") String id) {

        return ResponseEntity.ok(barrierlessCriteriaCheckService.findAllBarrierCriteriaCheckByLocationId(UUID.fromString(id)));
    }

    @GetMapping("/location/{location_id}/criteria/{criteria_id}")
    ResponseEntity<List<BarrierlessCriteriaCheck>> getBarrierlessCriteriaCheckByLocationAndCriteria(@PathVariable("location_id") String locationId,
                                                                                                    @PathVariable("criteria_id") String criteriaId) {

        return ResponseEntity.ok(barrierlessCriteriaCheckService.findAllBarrierCriteriaCheckByLocationIdAndCriteria(UUID.fromString(locationId), UUID.fromString(criteriaId)));
    }

    @GetMapping("/user/{user_id}")
    ResponseEntity<List<BarrierlessCriteriaCheck>> getBarrierlessCriteriaCheckByUser(@PathVariable("user_id") String id) {

        return ResponseEntity.ok(barrierlessCriteriaCheckService.findAllBarrierCriteriaCheckByUserId(UUID.fromString(id)));
    }
}
