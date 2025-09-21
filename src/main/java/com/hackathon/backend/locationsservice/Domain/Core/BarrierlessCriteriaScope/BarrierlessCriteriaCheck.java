package com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.Location;
import com.hackathon.backend.locationsservice.Security.Domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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

    private boolean hasIssue;

    private boolean barrierFreeRating;
}
