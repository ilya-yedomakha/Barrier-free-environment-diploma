package com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.Location;
import com.hackathon.backend.locationsservice.Security.Domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(schema = "geo_score_schema")
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

    private float barrierFreeRating;

    @NotNull
    @Column(nullable = false)
    private UUID createdBy;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @NotNull
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public void reassignTo(Location newLocation, EntityManager em) {
        BarrierlessCriteriaCheckEmbeddedId newId =
                new BarrierlessCriteriaCheckEmbeddedId(
                        newLocation.getId(),
                        this.getBarrierlessCriteriaCheckId().getBarrierlessCriteriaId(),
                        this.getBarrierlessCriteriaCheckId().getUserId()
                );

        BarrierlessCriteriaCheck newCheck = new BarrierlessCriteriaCheck();
        newCheck.setBarrierlessCriteriaCheckId(newId);
        newCheck.setLocation(newLocation);
        newCheck.setOtherFieldsFrom(this);

        em.persist(newCheck);
        em.remove(this);
    }


    public void setOtherFieldsFrom(BarrierlessCriteriaCheck other) {
        // barrierlessCriteria і user у тебе входять у EmbeddedId,
        // але це @ManyToOne зв’язки – їх теж треба виставити на новому об’єкті
        this.barrierlessCriteria = other.getBarrierlessCriteria();
        this.user = other.getUser();

        this.comment = other.getComment();
        this.hasIssue = other.isHasIssue();
        this.barrierFreeRating = other.getBarrierFreeRating();
        this.createdBy = other.getCreatedBy();
        this.createdAt = other.getCreatedAt();
        this.updatedAt = other.getUpdatedAt();
    }
}
