package com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.Base.BaseRegularCreateDTO;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteria;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaCheckEmbeddedId;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.Location;
import com.hackathon.backend.locationsservice.Domain.Enums.BarrierlessCriteriaRank;
import com.hackathon.backend.locationsservice.Security.Domain.User;
import jakarta.persistence.*;
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
public class BarrierlessCriteriaCheckCreateDTO extends BaseRegularCreateDTO {
    @NotNull
    public UUID locationId;
    @NotNull
    public UUID barrierlessCriteriaId;
    @NotNull
    public UUID userId;

    private String comment;

    private boolean hasIssue;

    private float barrierFreeRating;

    @NotNull
    public UUID createdBy;

    @NotNull
    public LocalDateTime createdAt = LocalDateTime.now();

    @NotNull
    public LocalDateTime updatedAt = LocalDateTime.now();

    @NotNull
    private UUID imageServiceId;
}
