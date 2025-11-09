package com.hackathon.backend.locationsservice.Domain.Core.LocationScope;

import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaGroup;
import com.hackathon.backend.locationsservice.Domain.Core.Base.BaseEntity;
import com.hackathon.backend.locationsservice.Domain.Core.Base.NamedEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "LocationTypes",schema = "geo_score_schema")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationType extends NamedEntity {

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "type")
    private Set<Location> locations;

    @ManyToOne
    @JoinColumn(name="barrierless_criteria_group_id", nullable = false)
    private BarrierlessCriteriaGroup barrierlessCriteriaGroup;

    @NotNull
    @Column(nullable = false)
    private UUID createdBy;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @NotNull
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}
