package com.hackathon.backend.locationsservice.Services.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.Result.Result;
import com.hackathon.backend.locationsservice.Result.EntityErrors.EntityError;
import com.hackathon.backend.locationsservice.Controllers.RequestDTO.Mappers.Read.BarrierlessCriteriaReadMapper;
import com.hackathon.backend.locationsservice.Controllers.RequestDTO.Read.BarrierlessCriteriaScope.BarrierlessCriteriaReadDTO;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteria;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BarrierlessCriteriaService {
    private final BarrierlessCriteriaRepository barrierlessCriteriaRepository;
    private final BarrierlessCriteriaReadMapper barrierlessCriteriaReadMapper;

    public Result<BarrierlessCriteria, BarrierlessCriteriaReadDTO> getById(UUID barrierlessCriteriaId) {
        Optional<BarrierlessCriteria> barrierlessCriteria = barrierlessCriteriaRepository.findById(barrierlessCriteriaId);
        if (barrierlessCriteria.isPresent()){
            Result<BarrierlessCriteria, BarrierlessCriteriaReadDTO> res = Result.success();
            res.setEntity(barrierlessCriteria.get());
            res.setEntityDTO(barrierlessCriteriaReadMapper.mapReadBarrierlessCriteria(barrierlessCriteria.get()));
            return res;
        } else return Result.failure(new EntityError<>(BarrierlessCriteria.class).notFound(barrierlessCriteriaId));
    }
}
