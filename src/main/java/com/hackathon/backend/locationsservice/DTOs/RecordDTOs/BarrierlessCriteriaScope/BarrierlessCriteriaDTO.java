package com.hackathon.backend.locationsservice.DTOs.RecordDTOs.BarrierlessCriteriaScope;

import java.util.UUID;

public record BarrierlessCriteriaDTO(
        UUID id,
        String name,
        String description,
        String rank
) {}