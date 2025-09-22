package com.hackathon.backend.locationsservice.DTOs.Mappers.LocationScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.LocationScope.LocationTypeCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.LocationScope.LocationTypeReadDTO;
import com.hackathon.backend.locationsservice.DTOs.Mappers.Base.BaseMapper;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaGroup;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.LocationType;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationTypeMapper implements BaseMapper<LocationType, LocationTypeReadDTO, LocationTypeCreateDTO> {
    private final BarrierlessCriteriaGroupRepository barrierlessCriteriaGroupRepository;
    @Override
    public LocationType toEntity(LocationTypeCreateDTO dto) {
        LocationType locationType = new LocationType();
        locationType.setName(dto.getName());
        locationType.setDescription(dto.getDescription());
        locationType.setCreatedBy(dto.getCreatedBy());
        BarrierlessCriteriaGroup group = barrierlessCriteriaGroupRepository
                .findById(dto.getBarrierlessCriteriaGroupId()).get();
        locationType.setBarrierlessCriteriaGroup(group);
        locationType.setCreatedAt(dto.getCreatedAt());
        locationType.setUpdatedAt(dto.getUpdatedAt());
        return locationType;
    }

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
}
