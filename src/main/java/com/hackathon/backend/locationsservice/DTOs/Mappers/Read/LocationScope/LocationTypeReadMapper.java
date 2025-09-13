package com.hackathon.backend.locationsservice.DTOs.Mappers.Read.LocationScope;

import com.hackathon.backend.locationsservice.DTOs.Mappers.Base.Read.BaseReadMapper;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.LocationScope.LocationTypeReadDTO;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.LocationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationTypeReadMapper implements BaseReadMapper<LocationType, LocationTypeReadDTO> {
    @Override
    public LocationTypeReadDTO toDto(LocationType LocationType) {
        LocationTypeReadDTO LocationTypeReadDTO = new LocationTypeReadDTO();
        LocationTypeReadDTO.setId(LocationType.getId());
        LocationTypeReadDTO.setName(LocationType.getName());
        LocationTypeReadDTO.setDescription(LocationType.getDescription());
        LocationTypeReadDTO.setBarrierlessCriteriaGroupId(LocationType.getBarrierlessCriteriaGroup().getId());
        LocationTypeReadDTO.setCreatedAt(LocationType.getCreatedAt());
        LocationTypeReadDTO.setCreatedBy(LocationType.getCreatedBy());
        LocationTypeReadDTO.setUpdatedAt(LocationType.getUpdatedAt());

        return LocationTypeReadDTO;
    }

    @Override
    public LocationType toEntity(LocationTypeReadDTO dto) {
        throw new UnsupportedOperationException("Not implemented in ReadMapper");
    }
}