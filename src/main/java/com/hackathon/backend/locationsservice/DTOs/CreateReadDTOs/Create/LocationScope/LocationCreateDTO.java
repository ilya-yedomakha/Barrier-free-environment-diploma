package com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.LocationScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.Base.BaseRegularCreateDTO;
import com.hackathon.backend.locationsservice.Domain.Enums.LocationStatusEnum;
import com.hackathon.backend.locationsservice.Domain.JSONB_POJOs.Contacts;
import com.hackathon.backend.locationsservice.Domain.JSONB_POJOs.Coordinates;
import com.hackathon.backend.locationsservice.Domain.JSONB_POJOs.WorkingHours;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class LocationCreateDTO extends BaseRegularCreateDTO {
    @NotNull
    @NotBlank
    public String name;

    @NotNull
    @NotBlank
    public String address;

    @NotNull
    public Coordinates coordinates;

    @NotNull
    public UUID type;

    public String description;

    public Contacts contacts;

    public WorkingHours workingHours;

    @NotNull
    public UUID createdBy;

    public UUID organizationId;

    public LocationStatusEnum status;

    public Integer overallAccessibilityScore;

    @NotNull
    public LocalDateTime createdAt = LocalDateTime.now();

    @NotNull
    public LocalDateTime updatedAt = LocalDateTime.now();

    @NotNull
    public LocalDateTime lastVerifiedAt;

    public String rejectionReason;

}
