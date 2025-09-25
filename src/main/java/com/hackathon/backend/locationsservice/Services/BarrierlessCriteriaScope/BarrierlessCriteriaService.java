package com.hackathon.backend.locationsservice.Services.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.BarrierlessCriteriaScope.BarrierlessCriteriaCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.BarrierlessCriteriaScope.BarrierlessCriteriaReadDTO;
import com.hackathon.backend.locationsservice.DTOs.Mappers.BarrierlessCriteriaScope.BarrierlessCriteriaMapper;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteria;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaType;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaRepository;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaTypeRepository;
import com.hackathon.backend.locationsservice.Result.EntityErrors.EntityError;
import com.hackathon.backend.locationsservice.Result.Result;
import com.hackathon.backend.locationsservice.Services.GeneralService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BarrierlessCriteriaService extends GeneralService<BarrierlessCriteriaMapper,BarrierlessCriteriaReadDTO,BarrierlessCriteriaCreateDTO,BarrierlessCriteria,BarrierlessCriteriaRepository> {

    private final BarrierlessCriteriaTypeRepository barrierlessCriteriaTypeRepository;

    BarrierlessCriteriaService(BarrierlessCriteriaRepository barrierlessCriteriaRepository, BarrierlessCriteriaMapper barrierlessCriteriaMapper, BarrierlessCriteriaTypeRepository barrierlessCriteriaTypeRepository){
        super(barrierlessCriteriaRepository,BarrierlessCriteria.class,barrierlessCriteriaMapper);
        this.barrierlessCriteriaTypeRepository = barrierlessCriteriaTypeRepository;
    }

    public Result<BarrierlessCriteria, BarrierlessCriteriaReadDTO> add(BarrierlessCriteriaCreateDTO barrierlessCriteriaCreateDTO) {
        Optional<BarrierlessCriteriaType> barrierlessCriteriaType = barrierlessCriteriaTypeRepository.findById(barrierlessCriteriaCreateDTO.getBarrierlessCriteriaTypeId());
        if (barrierlessCriteriaType.isEmpty()) {
            return Result.failure(EntityError.notFound(BarrierlessCriteriaType.class,barrierlessCriteriaCreateDTO.getBarrierlessCriteriaTypeId()));
        }
        BarrierlessCriteria newBarrierlessCriteria = mapper.toEntity(barrierlessCriteriaCreateDTO);
        if (newBarrierlessCriteria == null) {
            return Result.failure(EntityError.nullReference(type));
        }

        List<BarrierlessCriteria> barrierlessCriterias = repository.findAll();

        //TODO
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

    public List<BarrierlessCriteria> findAllByTypeId(UUID criteriaTypeId){

        return repository.findByBarrierlessCriteriaType_Id(criteriaTypeId);
    }

}
