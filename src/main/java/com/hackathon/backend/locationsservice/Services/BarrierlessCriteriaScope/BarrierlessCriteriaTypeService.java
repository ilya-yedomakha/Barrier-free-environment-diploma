package com.hackathon.backend.locationsservice.Services.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.BarrierlessCriteriaScope.BarrierlessCriteriaTypeCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.BarrierlessCriteriaScope.BarrierlessCriteriaTypeReadDTO;
import com.hackathon.backend.locationsservice.DTOs.Mappers.Create.BarrierlessCriteriaScope.BarrierlessCriteriaGroupCreateMapper;
import com.hackathon.backend.locationsservice.DTOs.Mappers.Create.BarrierlessCriteriaScope.BarrierlessCriteriaTypeCreateMapper;
import com.hackathon.backend.locationsservice.DTOs.Mappers.Read.BarrierlessCriteriaScope.BarrierlessCriteriaTypeReadMapper;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaGroup;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaType;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaGroupRepository;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaTypeRepository;
import com.hackathon.backend.locationsservice.Result.EntityErrors.EntityError;
import com.hackathon.backend.locationsservice.Result.Result;
import com.hackathon.backend.locationsservice.Services.GeneralService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BarrierlessCriteriaTypeService extends GeneralService<BarrierlessCriteriaTypeReadMapper, BarrierlessCriteriaTypeReadDTO, BarrierlessCriteriaType, BarrierlessCriteriaTypeRepository> {

    private final BarrierlessCriteriaTypeCreateMapper barrierlessCriteriaTypeCreateMapper;
    private final BarrierlessCriteriaGroupRepository barrierlessCriteriaGroupRepository;

    BarrierlessCriteriaTypeService(BarrierlessCriteriaTypeRepository barrierlessCriteriaTypeRepository, BarrierlessCriteriaTypeReadMapper barrierlessCriteriaTypeReadMapper, BarrierlessCriteriaTypeCreateMapper barrierlessCriteriaTypeCreateMapper, BarrierlessCriteriaGroupRepository barrierlessCriteriaGroupRepository){
        super(barrierlessCriteriaTypeRepository, BarrierlessCriteriaType.class,barrierlessCriteriaTypeReadMapper);
        this.barrierlessCriteriaTypeCreateMapper = barrierlessCriteriaTypeCreateMapper;
        this.barrierlessCriteriaGroupRepository = barrierlessCriteriaGroupRepository;
    }

    public Result<BarrierlessCriteriaType, BarrierlessCriteriaTypeReadDTO> add(BarrierlessCriteriaTypeCreateDTO barrierlessCriteriaTypeCreateDTO) {
        Optional<BarrierlessCriteriaGroup> barrierlessCriteriaGroup = barrierlessCriteriaGroupRepository.findById(barrierlessCriteriaTypeCreateDTO.getBarrierlessCriteriaGroupId());
        if (barrierlessCriteriaGroup.isEmpty()) {
            return Result.failure(EntityError.notFound(BarrierlessCriteriaType.class,barrierlessCriteriaTypeCreateDTO.getBarrierlessCriteriaGroupId()));
        }
        BarrierlessCriteriaType newBarrierlessCriteriaType = barrierlessCriteriaTypeCreateMapper.toEntity(barrierlessCriteriaTypeCreateDTO);
        if (newBarrierlessCriteriaType == null) {
            return Result.failure(EntityError.nullReference(type));
        }



        List<BarrierlessCriteriaType> barrierlessCriteriaTypes = repository.findAll();

        if(checkNameDuplicates(barrierlessCriteriaTypes,newBarrierlessCriteriaType.getName())){
            return Result.failure(EntityError.sameName(type, newBarrierlessCriteriaType.getName()));
        };

        String normalizedDescription = newBarrierlessCriteriaType.getDescription().toLowerCase().replace(" ", "");

        boolean duplicateDescriptions = barrierlessCriteriaTypes.stream()
                .map(e -> e.getDescription().toLowerCase().replace(" ", ""))
                .anyMatch(n -> n.equals(normalizedDescription));

        if (duplicateDescriptions){
            return Result.failure(EntityError.sameDesc(type, newBarrierlessCriteriaType.getDescription()));
        }



        BarrierlessCriteriaType savedBarrierlessCriteriaType = repository.save(newBarrierlessCriteriaType);
        Result<BarrierlessCriteriaType, BarrierlessCriteriaTypeReadDTO> res = Result.success();
        res.entity = savedBarrierlessCriteriaType;
        res.entityDTO = mapper.toDto(savedBarrierlessCriteriaType);

        return res;
    }

}
