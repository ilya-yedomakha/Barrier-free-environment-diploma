package com.hackathon.backend.locationsservice.Services.LocationScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.LocationScope.LocationTypeCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.LocationScope.LocationTypeReadDTO;
import com.hackathon.backend.locationsservice.DTOs.Mappers.Create.LocationScope.LocationTypeCreateMapper;
import com.hackathon.backend.locationsservice.DTOs.Mappers.Read.LocationScope.LocationTypeReadMapper;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaGroup;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.LocationType;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaGroupRepository;
import com.hackathon.backend.locationsservice.Repositories.LocationScope.LocationTypeRepository;
import com.hackathon.backend.locationsservice.Result.EntityErrors.EntityError;
import com.hackathon.backend.locationsservice.Result.Result;
import com.hackathon.backend.locationsservice.Services.GeneralService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationTypeService extends GeneralService<LocationTypeReadMapper, LocationTypeReadDTO, LocationType, LocationTypeRepository> {
    private final LocationTypeCreateMapper locationTypeCreateMapper;
    private final BarrierlessCriteriaGroupRepository barrierlessCriteriaGroupRepository;

    LocationTypeService(LocationTypeRepository locationTypeRepository, LocationTypeReadMapper locationTypeReadMapper, LocationTypeCreateMapper locationTypeCreateMapper, BarrierlessCriteriaGroupRepository barrierlessCriteriaGroupRepository) {
        super(locationTypeRepository, LocationType.class, locationTypeReadMapper);
        this.locationTypeCreateMapper = locationTypeCreateMapper;
        this.barrierlessCriteriaGroupRepository = barrierlessCriteriaGroupRepository;
    }

    public Result<LocationType, LocationTypeReadDTO> add(LocationTypeCreateDTO locationTypeCreateDTO) {
        Optional<BarrierlessCriteriaGroup> barrierlessCriteriaGroup = barrierlessCriteriaGroupRepository.findById(locationTypeCreateDTO.getBarrierlessCriteriaGroupId());
        if (barrierlessCriteriaGroup.isEmpty()) {
            return Result.failure(EntityError.notFound(BarrierlessCriteriaGroup.class,locationTypeCreateDTO.getBarrierlessCriteriaGroupId()));
        }
        LocationType newLocationType = locationTypeCreateMapper.toEntity(locationTypeCreateDTO);
        if (newLocationType == null) {
            return Result.failure(EntityError.nullReference(type));
        }

        List<LocationType> locationTypeNameDuplicates = repository.findAllByName(newLocationType.getName());
        if (locationTypeNameDuplicates != null && !locationTypeNameDuplicates.isEmpty()) {
            return Result.failure(EntityError.sameName(type, newLocationType.getName()));
        }

        //TODO: There can be same descriptions for different locations?
//        List<LocationType> locationTypeDescriptionDuplicates = repository.findAllByDescription(newLocationType.getDescription());
//        if (locationTypeDescriptionDuplicates != null && !locationTypeDescriptionDuplicates.isEmpty()) {
//            return Result.failure(EntityError.sameDesc(type, newLocationType.getDescription()));
//        }

        LocationType savedLocationType = repository.save(newLocationType);
        Result<LocationType, LocationTypeReadDTO> res = Result.success();
        res.entity = savedLocationType;
        res.entityDTO = mapper.toDto(savedLocationType);

        return res;

    }
}