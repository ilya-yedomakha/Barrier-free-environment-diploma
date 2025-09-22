package com.hackathon.backend.locationsservice.Services.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.BarrierlessCriteriaScope.BarrierlessCriteriaGroupCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.BarrierlessCriteriaScope.BarrierlessCriteriaGroupReadDTO;
import com.hackathon.backend.locationsservice.DTOs.Mappers.BarrierlessCriteriaScope.BarrierlessCriteriaGroupMapper;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaGroup;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaGroupRepository;
import com.hackathon.backend.locationsservice.Result.EntityErrors.EntityError;
import com.hackathon.backend.locationsservice.Result.Result;
import com.hackathon.backend.locationsservice.Services.GeneralService;
import org.springframework.stereotype.Service;

import java.util.List;

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


}
