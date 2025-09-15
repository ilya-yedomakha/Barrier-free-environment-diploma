package com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.Base.BaseRegularCreateDTO;
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
public class BarrierlessCriteriaGroupCreateDTO extends BaseRegularCreateDTO {

    public String name;

    public String description;

    @NotNull
    public UUID createdBy;

    @NotNull
    public LocalDateTime createdAt = LocalDateTime.now();

    @NotNull
    public LocalDateTime updatedAt = LocalDateTime.now();
}
