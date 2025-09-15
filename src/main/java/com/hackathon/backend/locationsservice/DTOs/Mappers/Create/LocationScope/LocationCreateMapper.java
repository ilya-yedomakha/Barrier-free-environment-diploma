package com.hackathon.backend.locationsservice.DTOs.Mappers.Create.LocationScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.LocationScope.LocationCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.Mappers.Base.Create.BaseCreateMapper;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.Location;


import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.LocationType;
import com.hackathon.backend.locationsservice.Repositories.LocationScope.LocationTypeRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationCreateMapper implements BaseCreateMapper<Location, LocationCreateDTO> {

    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    private final LocationTypeRepository locationTypeRepository;

    @Override
    public Location toEntity(LocationCreateDTO dto) {
        Point coordinates = null;
        if (dto.coordinates != null) {
            Coordinate coord = new Coordinate(dto.coordinates.getLat(), dto.coordinates.getLng());
            coordinates = geometryFactory.createPoint(coord);
        }

        Location location = new Location();
        location.setName(dto.name);
        location.setAddress(dto.address);
        location.setCoordinates(coordinates);
        LocationType locationType = locationTypeRepository.findById(dto.type).get();
        location.setType(locationType);
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

    @Override
    public LocationCreateDTO toDto(Location entity) {
        throw new UnsupportedOperationException("Not implemented in CreateMapper");
    }
}
