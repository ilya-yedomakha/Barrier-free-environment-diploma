package com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.Base.BaseRegularReadDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
public class BarrierlessCriteriaCheckReadDTO {

    public UUID locationId;

    public UUID barrierlessCriteriaId;

    public UUID userId;

    private String comment;

    private boolean hasIssue;

    private float barrierFreeRating;

    public UUID createdBy;

    public LocalDateTime createdAt;

    public LocalDateTime updatedAt;

    private UUID imageServiceId;
}
