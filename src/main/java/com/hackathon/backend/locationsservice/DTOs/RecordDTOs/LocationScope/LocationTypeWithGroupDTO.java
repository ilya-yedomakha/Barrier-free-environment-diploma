package com.hackathon.backend.locationsservice.DTOs.RecordDTOs.LocationScope;

import com.hackathon.backend.locationsservice.DTOs.RecordDTOs.BarrierlessCriteriaScope.BarrierlessCriteriaGroupDTO;

import java.util.UUID;

public record LocationTypeWithGroupDTO(
        UUID id,
        String name,
        String description,
        BarrierlessCriteriaGroupDTO group
) {}