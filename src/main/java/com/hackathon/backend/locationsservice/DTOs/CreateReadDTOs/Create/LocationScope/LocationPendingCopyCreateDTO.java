package com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.LocationScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.Base.BaseRegularCreateDTO;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.Location;
import com.hackathon.backend.locationsservice.Domain.Enums.LocationStatusEnum;
import com.hackathon.backend.locationsservice.Domain.JSONB_POJOs.Contacts;
import com.hackathon.backend.locationsservice.Domain.JSONB_POJOs.Coordinates;
import com.hackathon.backend.locationsservice.Domain.JSONB_POJOs.WorkingHours;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class LocationPendingCopyCreateDTO extends BaseRegularCreateDTO {
    UUID locationId;

    private String name;

    private String address;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "contacts", columnDefinition = "jsonb")
    private Contacts contacts;

    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "working_hours", columnDefinition = "jsonb")
    private WorkingHours workingHours;


    @Column(name = "organization_id")
    private UUID organizationId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LocationStatusEnum status = LocationStatusEnum.pending;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @NotNull
    @Column(nullable = false)
    private UUID updatedBy;

    @Column(columnDefinition = "TEXT")
    private String rejectionReason;

    private LocalDateTime rejectedAt;

    private UUID rejectedBy;

}
