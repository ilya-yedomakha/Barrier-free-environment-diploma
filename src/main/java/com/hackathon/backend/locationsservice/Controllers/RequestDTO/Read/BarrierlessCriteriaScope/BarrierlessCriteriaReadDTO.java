package com.hackathon.backend.locationsservice.Controllers.RequestDTO.Read.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.Controllers.RequestDTO.Read.Base.BaseReadDTO;
import com.hackathon.backend.locationsservice.Controllers.RequestDTO.Read.Base.BaseRegularReadDTO;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaType;
import com.hackathon.backend.locationsservice.Domain.Enums.BarrierlessCriteriaRank;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class BarrierlessCriteriaReadDTO extends BaseRegularReadDTO {
    private String name;

    private String description;

    private UUID barrierlessCriteriaType;

    private UUID createdBy;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    private BarrierlessCriteriaRank barrierlessCriteriaRank;
}
