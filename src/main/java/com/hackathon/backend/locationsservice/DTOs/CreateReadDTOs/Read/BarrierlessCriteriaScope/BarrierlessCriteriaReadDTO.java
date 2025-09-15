package com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.Base.BaseRegularReadDTO;
import com.hackathon.backend.locationsservice.Domain.Enums.BarrierlessCriteriaRank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BarrierlessCriteriaReadDTO extends BaseRegularReadDTO {
    public String name;

    public String description;

    public UUID barrierlessCriteriaTypeId;

    public UUID createdBy;

    public LocalDateTime createdAt;

    public LocalDateTime updatedAt;

    public BarrierlessCriteriaRank barrierlessCriteriaRank;
}
