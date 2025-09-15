package com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.Base.BaseRegularCreateDTO;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteria;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaGroup;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
public class BarrierlessCriteriaTypeCreateDTO extends BaseRegularCreateDTO {
    public String name;

    @NotBlank
    public String description;

    public UUID barrierlessCriteriaGroupId;

    @NotNull
    public UUID createdBy;

    @NotNull
    public LocalDateTime createdAt = LocalDateTime.now();

    @NotNull
    public LocalDateTime updatedAt = LocalDateTime.now();
}
