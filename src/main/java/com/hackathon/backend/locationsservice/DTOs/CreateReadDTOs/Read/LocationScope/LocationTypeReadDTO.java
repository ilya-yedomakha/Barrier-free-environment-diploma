package com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.LocationScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.Base.BaseRegularReadDTO;
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
public class LocationTypeReadDTO extends BaseRegularReadDTO {

    private String name;

    private String description;


    private UUID barrierlessCriteriaGroupId;

    private UUID createdBy;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();
}
