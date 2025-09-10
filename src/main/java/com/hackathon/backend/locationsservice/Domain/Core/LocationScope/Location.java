package com.hackathon.backend.locationsservice.Domain.Core.LocationScope;

import com.hackathon.backend.locationsservice.Domain.Core.Base.RegularEntity;
import com.hackathon.backend.locationsservice.Domain.Enums.LocationStatusEnum;
import com.hackathon.backend.locationsservice.Domain.JSONB_POJOs.Contacts;
import com.hackathon.backend.locationsservice.Domain.JSONB_POJOs.WorkingHours;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;


import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Locations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Location extends RegularEntity {

    @NotNull
    @NotBlank
    @Column(length = 255, nullable = false)
    private String name;

    @NotNull
    @NotBlank
    @Column(length = 500, nullable = false)
    private String address;

    @NotNull
    @Column(columnDefinition = "geometry(Point,4326)", nullable = false)
    private Point coordinates;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "location_type_id", referencedColumnName = "id", nullable = false)
    private LocationType type;

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

    @NotNull
    @Column(nullable = false)
    private UUID createdBy;

    @Column(name = "organization_id")
    private UUID organizationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LocationStatusEnum status;

    @Column(name = "overall_accessibility_score")
    private Integer overallAccessibilityScore;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @NotNull
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @NotNull
    @Column(nullable = false)
    private LocalDateTime lastVerifiedAt;

    @Column(columnDefinition = "TEXT")
    private String rejectionReason;
}

