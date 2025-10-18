package com.hackathon.backend.locationsservice.Result.EntityErrors;

import com.hackathon.backend.locationsservice.Result.Error;
import org.locationtech.jts.geom.Location;
import org.locationtech.jts.geom.Point;

import java.util.UUID;

public class LocationError {

    public static Error sameCoordinates(Point coordinates) {
        return new Error(
                Location.class.getSimpleName() + ".sameCoordinates",
                "There is already a " + Location.class.getSimpleName() + " with " + coordinates.getX() + " and " + coordinates.getY() + "present"
        );
    }

    public static Error locationMismatch(UUID locationId, UUID locationId2) {
        return new Error(
                Location.class.getSimpleName() + ".locationIdMismatch",
                "Given location id: " + locationId2 + " does not correspond to Location's real id:" + locationId
        );
    }

    public static Error invalidWorkingHours() {
        return new Error(
                Location.class.getSimpleName() + ".invalidWorkingHours",
                "Invalid working hours configuration: each day must have both 'open' and 'close' specified, or neither. " +
                        "You cannot set only one of them."
        );
    }

    public static Error notPublished(UUID locationId) {
        return new Error(
                Location.class.getSimpleName() + ".notPublished",
                "You can't edit location that was not verified yet."
        );
    }
}
