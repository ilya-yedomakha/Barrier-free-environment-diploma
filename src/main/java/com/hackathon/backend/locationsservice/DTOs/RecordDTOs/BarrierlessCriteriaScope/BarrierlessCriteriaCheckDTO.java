package com.hackathon.backend.locationsservice.DTOs.RecordDTOs.BarrierlessCriteriaScope;

import java.util.UUID;

public record BarrierlessCriteriaCheckDTO(
    UUID locationId,

    UUID barrierlessCriteriaId,

    UUID userId,

    String comment,

    boolean hasIssue
){}
