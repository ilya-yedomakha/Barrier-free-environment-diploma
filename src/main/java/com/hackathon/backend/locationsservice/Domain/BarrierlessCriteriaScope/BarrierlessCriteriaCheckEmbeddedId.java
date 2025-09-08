package com.hackathon.backend.locationsservice.Domain.BarrierlessCriteriaScope;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class BarrierlessCriteriaCheckEmbeddedId {

    private UUID locationId;

    private UUID barrierlessCriteriaId;

    private UUID userId;
}
