package com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.Base.BaseRegularCreateDTO;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaType;
import com.hackathon.backend.locationsservice.Domain.Enums.BarrierlessCriteriaRank;
import jakarta.validation.constraints.NotNull;
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
public class BarrierlessCriteriaCreateDTO extends BaseRegularCreateDTO {
    public String name;

    public String description;

    public UUID barrierlessCriteriaTypeId;

    @NotNull
    public UUID createdBy;

    @NotNull
    public LocalDateTime createdAt = LocalDateTime.now();

    @NotNull
    public LocalDateTime updatedAt = LocalDateTime.now();

    @NotNull
    public BarrierlessCriteriaRank barrierlessCriteriaRank;
}
