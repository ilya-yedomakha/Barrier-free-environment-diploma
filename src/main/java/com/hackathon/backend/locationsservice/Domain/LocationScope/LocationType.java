package com.hackathon.backend.locationsservice.Domain.LocationScope;

import com.hackathon.backend.locationsservice.Domain.BarrierlessCriteriaScope.BarrierlessCriteriaType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "LocationTypes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationType {
    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @NotBlank
    @Column(length = 255, nullable = false)
    private String name;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToOne
    private Location location;

    @ManyToOne
    @JoinColumn(name="barrierless_criteria_type_id", nullable = false)
    private BarrierlessCriteriaType barrierlessCriteriaType;

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
