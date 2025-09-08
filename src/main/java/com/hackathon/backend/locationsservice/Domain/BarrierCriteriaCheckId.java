package com.hackathon.backend.locationsservice.Domain;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class BarrierCriteriaCheckId {

    private UUID locationId;

    private UUID barrierlessCriteriaId;

    private UUID userId;
}
