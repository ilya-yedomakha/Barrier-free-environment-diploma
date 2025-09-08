package com.hackathon.backend.locationsservice.Controllers.RequestDTO.Create;

import com.hackathon.backend.locationsservice.Domain.Enums.LocationStatusEnum;
import com.hackathon.backend.locationsservice.Domain.JSONB_POJOs.Contacts;
import com.hackathon.backend.locationsservice.Domain.JSONB_POJOs.Coordinates;
import com.hackathon.backend.locationsservice.Domain.JSONB_POJOs.WorkingHours;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

public class LocationCreateDTO {
    @NotNull
    @NotBlank
    @Column(length = 255, nullable = false)
    public String name;

    @NotNull
    @NotBlank
    @Column(length = 500, nullable = false)
    public String address;

    public Coordinates coordinates;

    @NotNull
    @Column(name = "type", nullable = false)
    public UUID type;

    @Column(length = 100)
    public String category;

    @Column(columnDefinition = "TEXT")
    public String description;

    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "contacts", columnDefinition = "jsonb")
    public Contacts contacts;

    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "working_hours", columnDefinition = "jsonb")
    public WorkingHours workingHours;

    @NotNull
    @Column(nullable = false)
    public UUID createdBy;

    @Column(name = "organization_id")
    public UUID organizationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    public LocationStatusEnum status;

    @Column(name = "overall_accessibility_score")
    public Integer overallAccessibilityScore;

    @NotNull
    @Column(nullable = false)
    public LocalDateTime createdAt = LocalDateTime.now();

    @NotNull
    @Column(nullable = false)
    public LocalDateTime updatedAt = LocalDateTime.now();

    @NotNull
    @Column(nullable = false)
    public LocalDateTime lastVerifiedAt;

    @Column(columnDefinition = "TEXT")
    public String rejectionReason;

}
