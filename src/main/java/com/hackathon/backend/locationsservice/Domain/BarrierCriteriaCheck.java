package com.hackathon.backend.locationsservice.Domain;

import jakarta.persistence.*;

@Entity
public class BarrierCriteriaCheck {

    @EmbeddedId
    private BarrierCriteriaCheckId barrierCriteriaCheckId;

    @MapsId("locationId")
    @OneToOne
    @JoinColumn(name = "location_id", nullable = false, updatable = false)
    private Location location;

    @MapsId("barrierlessCriteriaId")
    @OneToOne
    @JoinColumn(name = "barrierless_criteria_id", nullable = false, updatable = false)
    private BarrierlessCriteria barrierlessCriteria;

    @MapsId
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    private String comment;

    private boolean barrierFree;

    private boolean barrierFreeRating;
}
