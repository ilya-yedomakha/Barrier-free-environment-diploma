package com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.LocationScope;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class RouteReadDTO {
    public Long id_0;

//    public LineString geom;   // залишаємо як є, для сумісності

    public double[][] coordinates;   // ✅ ДОДАТИ ЦЕ

    public String id;
    public String name;
    public String address;
    public LocalDateTime createdAt;
    public String createdBy;
    public String description;
    public LocalDateTime lastVerifiedAt;
    public String lastVerifiedBy;
    public String organizationId;
    public Integer overallAccessibilityScore;
    public String rejectionReason;
    public String status;
    public LocalDateTime updatedAt;
    public String updatedBy;
    public String locationTypeId;
    public String imageServiceId;
    public String start;
    public String end;
    public Double cost;
    public Double cost1;
    public String username;

    public UUID routeKey;
}
