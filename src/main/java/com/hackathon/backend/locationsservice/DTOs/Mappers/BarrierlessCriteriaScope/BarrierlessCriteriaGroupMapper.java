package com.hackathon.backend.locationsservice.DTOs.Mappers.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.BarrierlessCriteriaScope.BarrierlessCriteriaGroupCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.BarrierlessCriteriaScope.BarrierlessCriteriaGroupReadDTO;
import com.hackathon.backend.locationsservice.DTOs.Mappers.Base.BaseMapper;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BarrierlessCriteriaGroupMapper implements BaseMapper<BarrierlessCriteriaGroup, BarrierlessCriteriaGroupReadDTO, BarrierlessCriteriaGroupCreateDTO> {


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
    public BarrierlessCriteriaGroupReadDTO toDto(BarrierlessCriteriaGroup entity) {
        BarrierlessCriteriaGroupReadDTO dto = new BarrierlessCriteriaGroupReadDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }
}
