package com.hackathon.backend.locationsservice.Domain;


import com.hackathon.backend.locationsservice.Domain.Enums.CriteriaType;
import com.hackathon.backend.locationsservice.Domain.Enums.LocationTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "barrierless_criteria")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BarrierlessCriteria {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private LocationTypeEnum locationType;

    @Enumerated(EnumType.STRING)
    private CriteriaType criteriaType;
}
