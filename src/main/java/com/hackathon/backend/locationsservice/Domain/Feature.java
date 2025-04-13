package com.hackathon.backend.locationsservice.Domain;

import com.hackathon.backend.locationsservice.Domain.Enums.FeatureTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "accessibility_features")
public class Feature {
    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @Column(nullable = false)
    private UUID locationId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeatureTypeEnum type;

    @NotBlank
    @Column(length = 100)
    private String subtype;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Column(nullable = false)
    private Boolean status;

    @Min(1)
    @Max(5)
    private Integer qualityRating;

    private Boolean standardsCompliance;

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