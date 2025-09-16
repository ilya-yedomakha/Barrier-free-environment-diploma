package com.hackathon.backend.locationsservice.DTOs.Mappers.Create.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.BarrierlessCriteriaScope.BarrierlessCriteriaTypeCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.Mappers.Base.Create.BaseCreateMapper;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaType;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BarrierlessCriteriaTypeCreateMapper implements BaseCreateMapper<BarrierlessCriteriaType, BarrierlessCriteriaTypeCreateDTO> {

    private final BarrierlessCriteriaGroupRepository barrierlessCriteriaGroupRepository;

    @Override
    public BarrierlessCriteriaType toEntity(BarrierlessCriteriaTypeCreateDTO dto) {

        BarrierlessCriteriaType barrierlessCriteriaType = new BarrierlessCriteriaType();
        barrierlessCriteriaType.setName(dto.name);
        barrierlessCriteriaType.setDescription(dto.description);
        barrierlessCriteriaType.setCreatedBy(dto.createdBy);
        barrierlessCriteriaType.setCreatedAt(dto.createdAt);
        barrierlessCriteriaType.setUpdatedAt(dto.updatedAt);
        barrierlessCriteriaType.setBarrierlessCriteriaGroup(barrierlessCriteriaGroupRepository.findById(dto.getBarrierlessCriteriaGroupId()).get());


        return barrierlessCriteriaType;
    }

    @Override
    public BarrierlessCriteriaTypeCreateDTO toDto(BarrierlessCriteriaType entity) {
        throw new UnsupportedOperationException("Not implemented in CreateMapper");
    }
}
