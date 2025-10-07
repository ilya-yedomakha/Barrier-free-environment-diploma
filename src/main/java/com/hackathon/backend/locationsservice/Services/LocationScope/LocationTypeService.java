package com.hackathon.backend.locationsservice.Services.LocationScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.LocationScope.LocationTypeCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.LocationScope.LocationReadDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.LocationScope.LocationTypeReadDTO;
import com.hackathon.backend.locationsservice.DTOs.Mappers.LocationScope.LocationTypeMapper;
import com.hackathon.backend.locationsservice.DTOs.RecordDTOs.BarrierlessCriteriaScope.BarrierlessCriteriaDTO;
import com.hackathon.backend.locationsservice.DTOs.RecordDTOs.BarrierlessCriteriaScope.BarrierlessCriteriaGroupDTO;
import com.hackathon.backend.locationsservice.DTOs.RecordDTOs.BarrierlessCriteriaScope.BarrierlessCriteriaTypeDTO;
import com.hackathon.backend.locationsservice.DTOs.RecordDTOs.LocationScope.LocationTypeWithGroupDTO;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaGroup;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.Location;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.LocationType;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaGroupRepository;
import com.hackathon.backend.locationsservice.Repositories.LocationScope.LocationTypeRepository;
import com.hackathon.backend.locationsservice.Result.EntityErrors.EntityError;
import com.hackathon.backend.locationsservice.Result.Result;
import com.hackathon.backend.locationsservice.Services.GeneralService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LocationTypeService extends GeneralService<LocationTypeMapper, LocationTypeReadDTO, LocationTypeCreateDTO, LocationType, LocationTypeRepository> {
    private final BarrierlessCriteriaGroupRepository barrierlessCriteriaGroupRepository;

    LocationTypeService(LocationTypeRepository locationTypeRepository, LocationTypeMapper locationTypeMapper, BarrierlessCriteriaGroupRepository barrierlessCriteriaGroupRepository) {
        super(locationTypeRepository, LocationType.class, locationTypeMapper);
        this.barrierlessCriteriaGroupRepository = barrierlessCriteriaGroupRepository;
    }

    public Result<LocationType, LocationTypeReadDTO> add(LocationTypeCreateDTO locationTypeCreateDTO) {
        Optional<BarrierlessCriteriaGroup> barrierlessCriteriaGroup = barrierlessCriteriaGroupRepository.findById(locationTypeCreateDTO.getBarrierlessCriteriaGroupId());
        if (barrierlessCriteriaGroup.isEmpty()) {
            return Result.failure(EntityError.notFound(BarrierlessCriteriaGroup.class,locationTypeCreateDTO.getBarrierlessCriteriaGroupId()));
        }
        LocationType newLocationType = mapper.toEntity(locationTypeCreateDTO);
        if (newLocationType == null) {
            return Result.failure(EntityError.nullReference(type));
        }

        List<LocationType> locationTypes = repository.findAll();
        if(checkNameDuplicates(locationTypes,newLocationType.getName())){
            return Result.failure(EntityError.sameName(type, newLocationType.getName()));
        };

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

    public Result<LocationType, LocationTypeWithGroupDTO> getCriteriaTree(UUID id) {
        Optional<LocationType> locationTypeOptional = repository.findById(id);

        if (locationTypeOptional.isEmpty()) {
            return Result.failure(EntityError.notFound(type, id));
        }
        LocationType locationType = locationTypeOptional.get();

        BarrierlessCriteriaGroup group = locationType.getBarrierlessCriteriaGroup();

        List<BarrierlessCriteriaTypeDTO> typeDTOs = group.getBarrierlessCriteriaTypes().stream()
                .map(t -> new BarrierlessCriteriaTypeDTO(
                        t.getId(),
                        t.getName(),
                        t.getDescription(),
                        t.getBarrierlessCriterias().stream()
                                .map(c -> new BarrierlessCriteriaDTO(
                                        c.getId(),
                                        c.getName(),
                                        c.getDescription(),
                                        c.getBarrierlessCriteriaRank().name()
                                ))
                                .toList()
                ))
                .toList();

        LocationTypeWithGroupDTO locationTypeWithGroupDTO = new LocationTypeWithGroupDTO(
                locationType.getId(),
                locationType.getName(),
                locationType.getDescription(),
                new BarrierlessCriteriaGroupDTO(
                        group.getId(),
                        group.getName(),
                        group.getDescription(),
                        typeDTOs
                )
        );

        Result<LocationType, LocationTypeWithGroupDTO> res = Result.success();
        res.entity = locationType;
        res.entityDTO = locationTypeWithGroupDTO;

        return res;
    }
}