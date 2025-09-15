package com.hackathon.backend.locationsservice.DTOs.Mappers.Read.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.BarrierlessCriteriaScope.BarrierlessCriteriaGroupReadDTO;
import com.hackathon.backend.locationsservice.DTOs.Mappers.Base.Read.BaseReadMapper;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.BarrierlessCriteriaScope.BarrierlessCriteriaTypeReadDTO;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaGroup;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaType;
import org.springframework.stereotype.Service;

@Service
public class BarrierlessCriteriaTypeReadMapper implements BaseReadMapper<BarrierlessCriteriaType, BarrierlessCriteriaTypeReadDTO> {


    @Override
    public BarrierlessCriteriaTypeReadDTO toDto(BarrierlessCriteriaType entity) {
        BarrierlessCriteriaTypeReadDTO dto = new BarrierlessCriteriaTypeReadDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setBarrierlessCriteriaGroupId(entity.getBarrierlessCriteriaGroup().getId());

        return dto;
    }

    @Override
    public BarrierlessCriteriaType toEntity(BarrierlessCriteriaTypeReadDTO dto) {
        return null;
    }
}
