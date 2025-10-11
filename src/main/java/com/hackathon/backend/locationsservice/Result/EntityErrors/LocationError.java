package com.hackathon.backend.locationsservice.Result.EntityErrors;

import com.hackathon.backend.locationsservice.Result.Error;
import org.locationtech.jts.geom.Location;
import org.locationtech.jts.geom.Point;

public class LocationError {

    public static Error sameCoordinates(Point coordinates) {
        return new Error(
                Location.class.getSimpleName() + ".sameCoordinates",
                "There is already a " + Location.class.getSimpleName() + " with "+coordinates.getX()+" and "+coordinates.getY() + "present"
        );
    }

    public static Error invalidWorkingHours() {
        return new Error(
                Location.class.getSimpleName() + ".invalidWorkingHours",
                "Invalid working hours configuration: each day must have both 'open' and 'close' specified, or neither. " +
                        "You cannot set only one of them."
        );
    }

}
