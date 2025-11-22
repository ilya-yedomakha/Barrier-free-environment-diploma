package com.hackathon.backend.locationsservice.Result.EntityErrors;

import com.hackathon.backend.locationsservice.Result.Error;
import org.locationtech.jts.geom.Location;
import org.locationtech.jts.geom.Point;

import java.util.UUID;

public class UserError {
        public static Error notFound(String username) {
        return new Error(
                "User.notFound",
                "User with username '"+username+"' was not found!"
        );
    }
}
