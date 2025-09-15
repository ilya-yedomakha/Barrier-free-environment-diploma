package com.hackathon.backend.locationsservice.DTOs.Mappers.Create.LocationScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.LocationScope.LocationTypeCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.Mappers.Base.Create.BaseCreateMapper;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaGroup;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.LocationType;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationTypeCreateMapper implements BaseCreateMapper<LocationType, LocationTypeCreateDTO> {
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
    public LocationTypeCreateDTO toDto(LocationType entity) {
        return null;
    }
}
