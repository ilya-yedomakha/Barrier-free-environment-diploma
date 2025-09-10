package com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope;


import com.hackathon.backend.locationsservice.Domain.Core.Base.RegularEntity;
import com.hackathon.backend.locationsservice.Domain.Enums.BarrierlessCriteriaRank;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "barrierless_criteria")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BarrierlessCriteria extends RegularEntity {

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

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

    @Enumerated
    @NotNull
    private BarrierlessCriteriaRank barrierlessCriteriaRank;
}
