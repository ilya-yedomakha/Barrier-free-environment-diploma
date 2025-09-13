package com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.LocationScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.Base.BaseRegularReadDTO;
import com.hackathon.backend.locationsservice.Domain.Enums.LocationStatusEnum;
import com.hackathon.backend.locationsservice.Domain.JSONB_POJOs.Contacts;
import com.hackathon.backend.locationsservice.Domain.JSONB_POJOs.Coordinates;
import com.hackathon.backend.locationsservice.Domain.JSONB_POJOs.WorkingHours;
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
public class LocationReadDTO extends BaseRegularReadDTO {

    public String name;

    public String address;

    public Coordinates coordinates;

    public UUID type;

    public String category;

    public String description;

    public Contacts contacts;

    public WorkingHours workingHours;

    public UUID createdBy;

    public UUID organizationId;

    public LocationStatusEnum status;

    public Integer overallAccessibilityScore;

    public LocalDateTime createdAt = LocalDateTime.now();

    public LocalDateTime updatedAt = LocalDateTime.now();

    public LocalDateTime lastVerifiedAt;

    public String rejectionReason;
}
