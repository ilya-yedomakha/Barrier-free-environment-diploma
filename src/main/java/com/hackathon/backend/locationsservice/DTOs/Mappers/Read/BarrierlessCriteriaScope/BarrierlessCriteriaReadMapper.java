package com.hackathon.backend.locationsservice.DTOs.Mappers.Read.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.DTOs.Mappers.Base.Read.BaseReadMapper;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.BarrierlessCriteriaScope.BarrierlessCriteriaReadDTO;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteria;
import org.springframework.stereotype.Service;

@Service
public class BarrierlessCriteriaReadMapper implements BaseReadMapper<BarrierlessCriteria, BarrierlessCriteriaReadDTO> {

    @Override
    public BarrierlessCriteriaReadDTO toDto(BarrierlessCriteria entity) {
        BarrierlessCriteriaReadDTO dto = new BarrierlessCriteriaReadDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setBarrierlessCriteriaTypeId(entity.getBarrierlessCriteriaType().getId());
        dto.setBarrierlessCriteriaRank(entity.getBarrierlessCriteriaRank());
        return dto;
    }

    @Override
    public BarrierlessCriteria toEntity(BarrierlessCriteriaReadDTO dto) {
        throw new UnsupportedOperationException("Not implemented in ReadMapper");
    }
}
