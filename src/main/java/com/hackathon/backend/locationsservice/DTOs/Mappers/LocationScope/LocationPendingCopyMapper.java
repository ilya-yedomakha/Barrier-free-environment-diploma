package com.hackathon.backend.locationsservice.DTOs.Mappers.LocationScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.LocationScope.LocationCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.LocationScope.LocationPendingCopyCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.LocationScope.LocationPendingCopyReadDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.LocationScope.LocationReadDTO;
import com.hackathon.backend.locationsservice.DTOs.Mappers.Base.BaseMapper;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.Location;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.LocationPendingCopy;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.LocationType;
import com.hackathon.backend.locationsservice.Domain.JSONB_POJOs.Coordinates;
import com.hackathon.backend.locationsservice.Repositories.LocationScope.LocationRepository;
import com.hackathon.backend.locationsservice.Repositories.LocationScope.LocationTypeRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationPendingCopyMapper implements BaseMapper<LocationPendingCopy, LocationPendingCopyReadDTO, LocationPendingCopyCreateDTO> {

    private final LocationRepository locationRepository;

    @Override
    public LocationPendingCopy toEntity(LocationPendingCopyCreateDTO dto) {
        LocationPendingCopy locationPendingCopy = new LocationPendingCopy();
        Location location = locationRepository.findById(dto.getLocationId()).get();
        locationPendingCopy.setLocation(location);
        locationPendingCopy.setName(dto.getName());
        locationPendingCopy.setAddress(dto.getAddress());
        locationPendingCopy.setDescription(dto.getDescription());
        locationPendingCopy.setContacts(dto.getContacts());
        locationPendingCopy.setWorkingHours(dto.getWorkingHours());
        locationPendingCopy.setOrganizationId(dto.getOrganizationId());
        locationPendingCopy.setStatus(dto.getStatus());
        locationPendingCopy.setUpdatedAt(dto.getUpdatedAt());

        return locationPendingCopy;
    }

    @Override
    public LocationPendingCopyReadDTO toDto(LocationPendingCopy locationPendingCopy) {
        LocationPendingCopyReadDTO locationPendingCopyReadDTO = new LocationPendingCopyReadDTO();
        locationPendingCopyReadDTO.setId(locationPendingCopy.getId());
        locationPendingCopyReadDTO.setName(locationPendingCopy.getName());
        locationPendingCopyReadDTO.setAddress(locationPendingCopy.getAddress());
        locationPendingCopyReadDTO.setLocationId(locationPendingCopy.getLocation().getId());
        locationPendingCopyReadDTO.setDescription(locationPendingCopy.getDescription());
        locationPendingCopyReadDTO.setContacts(locationPendingCopy.getContacts());
        locationPendingCopyReadDTO.setWorkingHours(locationPendingCopy.getWorkingHours());
        locationPendingCopyReadDTO.setOrganizationId(locationPendingCopy.getOrganizationId());
        locationPendingCopyReadDTO.setStatus(locationPendingCopy.getStatus());
        locationPendingCopyReadDTO.setUpdatedAt(locationPendingCopy.getUpdatedAt());

        return locationPendingCopyReadDTO;
    }
}
