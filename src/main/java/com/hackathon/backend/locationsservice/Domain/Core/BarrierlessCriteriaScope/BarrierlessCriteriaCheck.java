package com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.Location;
import com.hackathon.backend.locationsservice.Domain.Core.User;
import jakarta.persistence.*;

@Entity
public class BarrierlessCriteriaCheck {

    @EmbeddedId
    private BarrierlessCriteriaCheckEmbeddedId barrierlessCriteriaCheckId;

    @MapsId("locationId")
    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false, updatable = false)
    private Location location;

    @MapsId("barrierlessCriteriaId")
    @ManyToOne
    @JoinColumn(name = "barrierless_criteria_id", nullable = false, updatable = false)
    private BarrierlessCriteria barrierlessCriteria;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    private String comment;

    private boolean barrierFree;

    private boolean barrierFreeRating;
}
