package com.hackathon.backend.locationsservice.Result.EntityErrors;

import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteria;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaCheck;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.LocationType;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.Route;
import com.hackathon.backend.locationsservice.Result.Error;

import java.util.UUID;

public class RouteError {
    public static com.hackathon.backend.locationsservice.Result.Error notFound(UUID routKey) {
        return new Error(
                Route.class.getSimpleName() + ".NotFound",
                "Provided " + Route.class.getSimpleName() + " with routeKey: " + routKey + " was not found."
        );
    }

    public static com.hackathon.backend.locationsservice.Result.Error coordinateTransformFailed(Double lat,Double lng) {
        return new Error(
                Route.class.getSimpleName() + ".CoordinateTransformFailed",
                "Provided Coordinates (" + lat + ", "+ lng + ") cannot be transformed to 5564 from 4326"
        );
    }

    public static com.hackathon.backend.locationsservice.Result.Error coordinateEmpty() {
        return new Error(
                Route.class.getSimpleName() + ".StartCoordinateEmpty",
                "Start coordinates must be provided!"
        );
    }
}
