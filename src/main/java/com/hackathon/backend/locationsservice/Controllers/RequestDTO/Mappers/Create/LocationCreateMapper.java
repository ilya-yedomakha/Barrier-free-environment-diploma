package com.hackathon.backend.locationsservice.Controllers.RequestDTO.Mappers.Create;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.backend.locationsservice.Controllers.RequestDTO.Create.LocationCreateDTO;
import com.hackathon.backend.locationsservice.Domain.Location;


import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;

@Service
public class LocationCreateMapper {
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    public Location mapCreateLocation(LocationCreateDTO dto) {
        Point coordinates = null;
        if (dto.coordinates != null) {
            Coordinate coord = new Coordinate(dto.coordinates.getLat(), dto.coordinates.getLng());
            coordinates = geometryFactory.createPoint(coord);
        }

        Location location = new Location();
        location.setName(dto.name);
        location.setAddress(dto.address);
        location.setCoordinates(coordinates);
        location.setType(dto.type);
        location.setCategory(dto.category);
        location.setDescription(dto.description);
        location.setContacts(dto.contacts);
        location.setWorkingHours(dto.workingHours);
        location.setCreatedBy(dto.createdBy);
        location.setOrganizationId(dto.organizationId);
        location.setStatus(dto.status);
        location.setOverallAccessibilityScore(dto.overallAccessibilityScore);
        location.setCreatedAt(dto.createdAt);
        location.setUpdatedAt(dto.updatedAt);
        location.setLastVerifiedAt(dto.lastVerifiedAt);
        location.setRejectionReason(dto.rejectionReason);

        return location;
    }
}