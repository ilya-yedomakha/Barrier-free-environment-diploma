package com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.LocationScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.Base.BaseRegularCreateDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LocationTypeCreateDTO extends BaseRegularCreateDTO {

    @NotNull
    private String name;

    private String description;

    @NotNull
    private UUID barrierlessCriteriaGroupId;

    @NotNull
    private UUID createdBy;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime updatedAt;
}
