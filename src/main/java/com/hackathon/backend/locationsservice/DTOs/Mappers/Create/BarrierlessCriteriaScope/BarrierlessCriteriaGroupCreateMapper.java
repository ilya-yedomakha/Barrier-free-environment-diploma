package com.hackathon.backend.locationsservice.DTOs.Mappers.Create.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.BarrierlessCriteriaScope.BarrierlessCriteriaGroupCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.Mappers.Base.Create.BaseCreateMapper;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BarrierlessCriteriaGroupCreateMapper implements BaseCreateMapper<BarrierlessCriteriaGroup, BarrierlessCriteriaGroupCreateDTO> {


    @Override
    public BarrierlessCriteriaGroup toEntity(BarrierlessCriteriaGroupCreateDTO dto) {

        BarrierlessCriteriaGroup barrierlessCriteriaGroup = new BarrierlessCriteriaGroup();
        barrierlessCriteriaGroup.setName(dto.name);
        barrierlessCriteriaGroup.setDescription(dto.description);
        barrierlessCriteriaGroup.setCreatedBy(dto.createdBy);
        barrierlessCriteriaGroup.setCreatedAt(dto.createdAt);
        barrierlessCriteriaGroup.setUpdatedAt(dto.updatedAt);

        return barrierlessCriteriaGroup;
    }

    @Override
    public BarrierlessCriteriaGroupCreateDTO toDto(BarrierlessCriteriaGroup entity) {
        throw new UnsupportedOperationException("Not implemented in CreateMapper");
    }
}
