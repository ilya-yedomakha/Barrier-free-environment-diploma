package com.hackathon.backend.locationsservice.Controllers.RequestDTO.Mappers.Read;

import com.hackathon.backend.locationsservice.Controllers.RequestDTO.Read.LocationReadDTO;
import com.hackathon.backend.locationsservice.Domain.JSONB_POJOs.Coordinates;
import com.hackathon.backend.locationsservice.Domain.LocationScope.Location;
import org.springframework.stereotype.Service;

@Service
public class LocationReadMapper {
    public LocationReadDTO mapReadLocation(Location location) {
        LocationReadDTO locationReadDTO = new LocationReadDTO();
        locationReadDTO.setId(location.getId());
        locationReadDTO.setName(location.getName());
        locationReadDTO.setAddress(location.getAddress());
        locationReadDTO.setCoordinates(new Coordinates(location.getCoordinates().getX(), location.getCoordinates().getY()));
        locationReadDTO.setType(location.getType().getId());
        locationReadDTO.setCategory(location.getCategory());
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

        return locationReadDTO;
    }
}