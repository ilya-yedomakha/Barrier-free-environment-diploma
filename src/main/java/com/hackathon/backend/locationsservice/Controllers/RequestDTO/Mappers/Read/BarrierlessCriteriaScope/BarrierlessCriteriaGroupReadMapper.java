package com.hackathon.backend.locationsservice.Controllers.RequestDTO.Mappers.Read.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.Controllers.RequestDTO.Mappers.Base.Read.BaseReadMapper;
import com.hackathon.backend.locationsservice.Controllers.RequestDTO.Read.BarrierlessCriteriaScope.BarrierlessCriteriaGroupReadDTO;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaGroup;
import org.springframework.stereotype.Service;

@Service
public class BarrierlessCriteriaGroupReadMapper implements BaseReadMapper<BarrierlessCriteriaGroup, BarrierlessCriteriaGroupReadDTO> {


    @Override
    public BarrierlessCriteriaGroupReadDTO toDto(BarrierlessCriteriaGroup entity) {
        return null;
    }

    @Override
    public BarrierlessCriteriaGroup toEntity(BarrierlessCriteriaGroupReadDTO dto) {
        return null;
    }
}
