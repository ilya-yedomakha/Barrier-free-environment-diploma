package com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope;

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
@Table(name = "BarrierlessCriteriaTypes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BarrierlessCriteriaType extends NamedEntity {

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name="barrierless_criteria_group_id", nullable = false)
    private BarrierlessCriteriaGroup barrierlessCriteriaGroup;

    @OneToMany(mappedBy="barrierlessCriteriaType")
    private Set<BarrierlessCriteria> BarrierlessCriterias;

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
