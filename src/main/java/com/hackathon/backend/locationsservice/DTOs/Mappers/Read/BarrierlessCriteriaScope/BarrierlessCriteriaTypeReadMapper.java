package com.hackathon.backend.locationsservice.DTOs.Mappers.Read.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.DTOs.Mappers.Base.Read.BaseReadMapper;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.BarrierlessCriteriaScope.BarrierlessCriteriaTypeReadDTO;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaType;
import org.springframework.stereotype.Service;

@Service
public class BarrierlessCriteriaTypeReadMapper implements BaseReadMapper<BarrierlessCriteriaType, BarrierlessCriteriaTypeReadDTO> {


    @Override
    public BarrierlessCriteriaTypeReadDTO toDto(BarrierlessCriteriaType entity) {
        return null;
    }

    @Override
    public BarrierlessCriteriaType toEntity(BarrierlessCriteriaTypeReadDTO dto) {
        return null;
    }
}
