package com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BarrierlessCriteriaCheckEmbeddedId implements Serializable {
    private UUID locationId;
    private UUID barrierlessCriteriaId;
    private UUID userId;
}
