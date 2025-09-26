package com.hackathon.backend.locationsservice.Services.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.BarrierlessCriteriaScope.BarrierlessCriteriaCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.BarrierlessCriteriaScope.BarrierlessCriteriaGroupCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.BarrierlessCriteriaScope.BarrierlessCriteriaGroupReadDTO;
import com.hackathon.backend.locationsservice.DTOs.Mappers.BarrierlessCriteriaScope.BarrierlessCriteriaGroupMapper;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteria;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaGroup;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaGroupRepository;
import com.hackathon.backend.locationsservice.Result.EntityErrors.EntityError;
import com.hackathon.backend.locationsservice.Result.Result;
import com.hackathon.backend.locationsservice.Services.GeneralService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BarrierlessCriteriaGroupService extends GeneralService<BarrierlessCriteriaGroupMapper, BarrierlessCriteriaGroupReadDTO, BarrierlessCriteriaGroupCreateDTO, BarrierlessCriteriaGroup,BarrierlessCriteriaGroupRepository> {

    private final BarrierlessCriteriaGroupMapper barrierlessCriteriaGroupMapper;

    BarrierlessCriteriaGroupService(BarrierlessCriteriaGroupRepository barrierlessCriteriaGroupRepository, BarrierlessCriteriaGroupMapper barrierlessCriteriaGroupMapper){
        super(barrierlessCriteriaGroupRepository,BarrierlessCriteriaGroup.class,barrierlessCriteriaGroupMapper);
        this.barrierlessCriteriaGroupMapper = barrierlessCriteriaGroupMapper;
    }

    public Result<BarrierlessCriteriaGroup, BarrierlessCriteriaGroupReadDTO> add(BarrierlessCriteriaGroupCreateDTO barrierlessCriteriaGroupCreateDTO) {
        BarrierlessCriteriaGroup newBarrierlessCriteriaGroup = barrierlessCriteriaGroupMapper.toEntity(barrierlessCriteriaGroupCreateDTO);
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

    //TODO
    public Result<BarrierlessCriteriaGroup, BarrierlessCriteriaGroupReadDTO> update(UUID groupId, BarrierlessCriteriaGroupCreateDTO updateDTO) {

        BarrierlessCriteriaGroup criteriaGroup = repository.findById(groupId).orElse(null);

        if(criteriaGroup == null){
            return Result.failure(EntityError.notFound(type,groupId));
        }

        criteriaGroup.setName(updateDTO.getName());
        criteriaGroup.setDescription(updateDTO.getDescription());
        criteriaGroup.setUpdatedAt(LocalDateTime.now());
        criteriaGroup = repository.save(criteriaGroup);

        Result<BarrierlessCriteriaGroup, BarrierlessCriteriaGroupReadDTO> res = Result.success();
        res.entity = criteriaGroup;
        res.entityDTO = mapper.toDto(criteriaGroup);
        return res;
    }


}
