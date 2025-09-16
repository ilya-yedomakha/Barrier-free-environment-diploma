package com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.Domain.Core.Base.NamedEntity;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.LocationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "BarrierlessCriteriaGroups")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BarrierlessCriteriaGroup extends NamedEntity {


    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy="barrierlessCriteriaGroup")
    private Set<LocationType> locationTypes;

    @OneToMany(mappedBy="barrierlessCriteriaGroup")
    private Set<BarrierlessCriteriaType> barrierlessCriteriaTypes;

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
