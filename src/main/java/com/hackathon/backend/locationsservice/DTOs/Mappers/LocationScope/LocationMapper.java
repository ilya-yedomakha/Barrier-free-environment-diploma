package com.hackathon.backend.locationsservice.DTOs.Mappers.LocationScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.LocationScope.LocationCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.LocationScope.LocationReadDTO;
import com.hackathon.backend.locationsservice.DTOs.Mappers.Base.BaseMapper;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.Location;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.LocationType;
import com.hackathon.backend.locationsservice.Domain.JSONB_POJOs.Coordinates;
import com.hackathon.backend.locationsservice.Repositories.LocationScope.LocationTypeRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationMapper implements BaseMapper<Location, LocationReadDTO, LocationCreateDTO> {

    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 5564);
    private final LocationTypeRepository locationTypeRepository;

    @Override
    public Location toEntity(LocationCreateDTO dto) {
        Point coordinates = null;
        if (dto.coordinates != null) {
            Coordinate coord = new Coordinate(dto.coordinates.getLng(), dto.coordinates.getLat());
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
        location.setImageServiceId(dto.getImageServiceId());

        return location;
    }

    @Override
    public LocationReadDTO toDto(Location location) {
        LocationReadDTO locationReadDTO = new LocationReadDTO();
        locationReadDTO.setId(location.getId());
        locationReadDTO.setName(location.getName());
        locationReadDTO.setAddress(location.getAddress());
        locationReadDTO.setCoordinates(new Coordinates(location.getCoordinates().getX(), location.getCoordinates().getY()));
        locationReadDTO.setType(location.getType().getId());
        locationReadDTO.setDescription(location.getDescription());
        locationReadDTO.setContacts(location.getContacts());
        locationReadDTO.setWorkingHours(location.getWorkingHours());
        locationReadDTO.setCreatedBy(location.getCreatedBy());
        locationReadDTO.setOrganizationId(location.getOrganizationId());
        locationReadDTO.setStatus(location.getStatus());
        locationReadDTO.setOverallAccessibilityScore(location.getOverallAccessibilityScore());
        locationReadDTO.setCreatedAt(location.getCreatedAt());
        locationReadDTO.setUpdatedAt(location.getUpdatedAt());
        locationReadDTO.setLastVerifiedAt(location.getLastVerifiedAt());
        locationReadDTO.setRejectionReason(location.getRejectionReason());
        locationReadDTO.setImageServiceId(location.getImageServiceId());

        return locationReadDTO;
    }
}
