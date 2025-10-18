package com.hackathon.backend.locationsservice.Result.EntityErrors;

import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteria;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaCheck;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.LocationType;
import com.hackathon.backend.locationsservice.Result.Error;
import org.locationtech.jts.geom.Location;
import org.locationtech.jts.geom.Point;

import java.util.UUID;

public class CheckError {

    public static Error mismatch(UUID locationTypeId, UUID barrierlessCriteriaId) {
        return new Error(
                BarrierlessCriteriaCheck.class.getSimpleName() + ".LocTypeMismatch",
                "Provided " + BarrierlessCriteria.class.getSimpleName() + " with id: " + barrierlessCriteriaId + " has no match with a " + LocationType.class.getSimpleName() + " with id: " + locationTypeId
        );
    }
}
