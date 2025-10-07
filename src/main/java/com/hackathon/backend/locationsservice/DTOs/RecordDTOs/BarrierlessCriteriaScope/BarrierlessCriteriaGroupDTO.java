package com.hackathon.backend.locationsservice.DTOs.RecordDTOs.BarrierlessCriteriaScope;

import java.util.List;
import java.util.UUID;

public record BarrierlessCriteriaGroupDTO(
        UUID id,
        String name,
        String description,
        List<BarrierlessCriteriaTypeDTO> types
) {}