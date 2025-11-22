package com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope;


import com.hackathon.backend.locationsservice.Domain.Core.Base.BaseEntity;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.LocationType;
import com.hackathon.backend.locationsservice.Domain.Enums.BarrierlessCriteriaRank;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "barrierless_criteria",schema = "geo_score_schema")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BarrierlessCriteria extends BaseEntity {

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name="barrierless_criteria_type_id", nullable = false)
    private BarrierlessCriteriaType barrierlessCriteriaType;

    @OneToMany(mappedBy="barrierlessCriteria")
    private Set<BarrierlessCriteriaCheck> barrierlessCriteriaChecks;

    @NotNull
    @Column(nullable = false)
    private UUID createdBy;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @NotNull
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Enumerated(value = EnumType.STRING)
    @Column(name = "rank", nullable = false)
    @ColumnDefault("'moderate'")
    private BarrierlessCriteriaRank barrierlessCriteriaRank;
}
