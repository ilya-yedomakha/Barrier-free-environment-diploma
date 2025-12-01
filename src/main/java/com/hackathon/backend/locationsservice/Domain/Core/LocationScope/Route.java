package com.hackathon.backend.locationsservice.Domain.Core.LocationScope;

import com.hackathon.backend.locationsservice.Domain.Core.Base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.LineString;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "shortest_routes_history", schema = "geo_score_schema")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Route{

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id_0;

    @Column(columnDefinition = "geometry(LineString,5564)")
    private LineString geom;

    @Column
    private String id;

    @Column
    private String name;

    @Column(length = 500)
    private String address;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "last_verified_at")
    private LocalDateTime lastVerifiedAt;

    @Column(name = "last_verified_by")
    private String lastVerifiedBy;

    @Column(name = "organization_id")
    private String organizationId;

    @Column(name = "overall_accessibility_score")
    private Integer overallAccessibilityScore;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "status")
    private String status;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "location_type_id")
    private String locationTypeId;

    @Column(name = "image_service_id")
    private String imageServiceId;

    @Column(name = "start")
    private String start;

    @Column(name = "\"end\"")
    private String end;

    @Column
    private Double cost;

    @Column
    private Double cost1;

    @Column
    private String username;

    @Column
    private UUID routeKey;
}
