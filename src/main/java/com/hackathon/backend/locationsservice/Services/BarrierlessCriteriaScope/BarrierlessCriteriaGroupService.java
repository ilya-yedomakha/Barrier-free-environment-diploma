package com.hackathon.backend.locationsservice.Services.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.BarrierlessCriteriaScope.BarrierlessCriteriaGroupCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.LocationScope.LocationTypeCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.LocationScope.LocationTypeReadDTO;
import com.hackathon.backend.locationsservice.DTOs.Mappers.Create.BarrierlessCriteriaScope.BarrierlessCriteriaGroupCreateMapper;
import com.hackathon.backend.locationsservice.DTOs.Mappers.Create.LocationScope.LocationCreateMapper;
import com.hackathon.backend.locationsservice.DTOs.Mappers.Read.BarrierlessCriteriaScope.BarrierlessCriteriaGroupReadMapper;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.BarrierlessCriteriaScope.BarrierlessCriteriaGroupReadDTO;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaGroup;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.Location;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.LocationType;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaGroupRepository;
import com.hackathon.backend.locationsservice.Result.EntityErrors.EntityError;
import com.hackathon.backend.locationsservice.Result.Result;
import com.hackathon.backend.locationsservice.Services.GeneralService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BarrierlessCriteriaGroupService extends GeneralService<BarrierlessCriteriaGroupReadMapper, BarrierlessCriteriaGroupReadDTO, BarrierlessCriteriaGroup,BarrierlessCriteriaGroupRepository> {

    private final BarrierlessCriteriaGroupCreateMapper barrierlessCriteriaGroupCreateMapper;

    BarrierlessCriteriaGroupService(BarrierlessCriteriaGroupRepository barrierlessCriteriaGroupRepository, BarrierlessCriteriaGroupReadMapper barrierlessCriteriaGroupReadMapper, BarrierlessCriteriaGroupCreateMapper barrierlessCriteriaGroupCreateMapper){
        super(barrierlessCriteriaGroupRepository,BarrierlessCriteriaGroup.class,barrierlessCriteriaGroupReadMapper);
        this.barrierlessCriteriaGroupCreateMapper = barrierlessCriteriaGroupCreateMapper;
    }

    public Result<BarrierlessCriteriaGroup, BarrierlessCriteriaGroupReadDTO> add(BarrierlessCriteriaGroupCreateDTO barrierlessCriteriaGroupCreateDTO) {
        BarrierlessCriteriaGroup newBarrierlessCriteriaGroup = barrierlessCriteriaGroupCreateMapper.toEntity(barrierlessCriteriaGroupCreateDTO);
        if (newBarrierlessCriteriaGroup == null) {
            return Result.failure(EntityError.nullReference(type));
        }

        List<BarrierlessCriteriaGroup> barrierlessCriteriaGroups = repository.findAll();
        if(checkNameDuplicates(barrierlessCriteriaGroups,newBarrierlessCriteriaGroup.getName())){
            return Result.failure(EntityError.sameName(type, newBarrierlessCriteriaGroup.getName()));
        };

        String normalizedDescription = newBarrierlessCriteriaGroup.getDescription().toLowerCase().replace(" ", "");

        boolean duplicateDescriptions = barrierlessCriteriaGroups.stream()
                .map(e -> e.getDescription().toLowerCase().replace(" ", ""))
                .anyMatch(n -> n.equals(normalizedDescription));

        if (duplicateDescriptions){
            return Result.failure(EntityError.sameDesc(type, newBarrierlessCriteriaGroup.getDescription()));
        }

        BarrierlessCriteriaGroup savedBarrierlessCriteriaGroup = repository.save(newBarrierlessCriteriaGroup);
        Result<BarrierlessCriteriaGroup, BarrierlessCriteriaGroupReadDTO> res = Result.success();
        res.entity = savedBarrierlessCriteriaGroup;
        res.entityDTO = mapper.toDto(savedBarrierlessCriteriaGroup);

        return res;

    }


}
