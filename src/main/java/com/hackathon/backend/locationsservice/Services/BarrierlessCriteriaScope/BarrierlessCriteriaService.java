package com.hackathon.backend.locationsservice.Services.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.BarrierlessCriteriaScope.BarrierlessCriteriaCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.BarrierlessCriteriaScope.BarrierlessCriteriaReadDTO;
import com.hackathon.backend.locationsservice.DTOs.Mappers.Create.BarrierlessCriteriaScope.BarrierlessCriteriaCreateMapper;
import com.hackathon.backend.locationsservice.DTOs.Mappers.Read.BarrierlessCriteriaScope.BarrierlessCriteriaReadMapper;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteria;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaGroup;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaType;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaGroupRepository;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaRepository;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaTypeRepository;
import com.hackathon.backend.locationsservice.Result.EntityErrors.EntityError;
import com.hackathon.backend.locationsservice.Result.Result;
import com.hackathon.backend.locationsservice.Services.GeneralService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BarrierlessCriteriaService extends GeneralService<BarrierlessCriteriaReadMapper,BarrierlessCriteriaReadDTO,BarrierlessCriteria,BarrierlessCriteriaRepository> {

    private final BarrierlessCriteriaCreateMapper barrierlessCriteriaCreateMapper;
    private final BarrierlessCriteriaTypeRepository barrierlessCriteriaTypeRepository;

    BarrierlessCriteriaService(BarrierlessCriteriaRepository barrierlessCriteriaRepository, BarrierlessCriteriaReadMapper barrierlessCriteriaReadMapper, BarrierlessCriteriaCreateMapper barrierlessCriteriaCreateMapper, BarrierlessCriteriaTypeRepository barrierlessCriteriaTypeRepository){
        super(barrierlessCriteriaRepository,BarrierlessCriteria.class,barrierlessCriteriaReadMapper);
        this.barrierlessCriteriaCreateMapper = barrierlessCriteriaCreateMapper;
        this.barrierlessCriteriaTypeRepository = barrierlessCriteriaTypeRepository;
    }

    public Result<BarrierlessCriteria, BarrierlessCriteriaReadDTO> add(BarrierlessCriteriaCreateDTO barrierlessCriteriaCreateDTO) {
        Optional<BarrierlessCriteriaType> barrierlessCriteriaType = barrierlessCriteriaTypeRepository.findById(barrierlessCriteriaCreateDTO.getBarrierlessCriteriaTypeId());
        if (barrierlessCriteriaType.isEmpty()) {
            return Result.failure(EntityError.notFound(BarrierlessCriteriaType.class,barrierlessCriteriaCreateDTO.getBarrierlessCriteriaTypeId()));
        }
        BarrierlessCriteria newBarrierlessCriteria = barrierlessCriteriaCreateMapper.toEntity(barrierlessCriteriaCreateDTO);
        if (newBarrierlessCriteria == null) {
            return Result.failure(EntityError.nullReference(type));
        }

        List<BarrierlessCriteria> barrierlessCriterias = repository.findAll();

        String normalizedDescription = newBarrierlessCriteria.getDescription().toLowerCase().replace(" ", "");

        boolean duplicateDescriptions = barrierlessCriterias.stream()
                .map(e -> e.getDescription().toLowerCase().replace(" ", ""))
                .anyMatch(n -> n.equals(normalizedDescription));

        if (duplicateDescriptions){
            return Result.failure(EntityError.sameDesc(type, newBarrierlessCriteria.getDescription()));
        }



        BarrierlessCriteria savedBarrierlessCriteria = repository.save(newBarrierlessCriteria);
        Result<BarrierlessCriteria, BarrierlessCriteriaReadDTO> res = Result.success();
        res.entity = savedBarrierlessCriteria;
        res.entityDTO = mapper.toDto(savedBarrierlessCriteria);

        return res;
    }
}
