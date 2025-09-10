package com.hackathon.backend.locationsservice.Controllers.RequestDTO.Mappers.Read;

import com.hackathon.backend.locationsservice.Controllers.RequestDTO.Read.BarrierlessCriteriaScope.BarrierlessCriteriaReadDTO;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteria;
import com.hackathon.backend.locationsservice.Domain.JSONB_POJOs.Coordinates;
import org.springframework.stereotype.Service;

@Service
public class BarrierlessCriteriaReadMapper {
    public BarrierlessCriteriaReadDTO mapReadBarrierlessCriteria(BarrierlessCriteria barrierlessCriteria) {
        BarrierlessCriteriaReadDTO barrierlessCriteriaReadDTO = new BarrierlessCriteriaReadDTO();
        barrierlessCriteriaReadDTO.setId(barrierlessCriteria.getId());
        barrierlessCriteriaReadDTO.setName(barrierlessCriteria.getName());
        barrierlessCriteriaReadDTO.setDescription(barrierlessCriteria.getDescription());
        barrierlessCriteriaReadDTO.setCreatedBy(barrierlessCriteria.getCreatedBy());
        barrierlessCriteriaReadDTO.setCreatedAt(barrierlessCriteria.getCreatedAt());
        barrierlessCriteriaReadDTO.setUpdatedAt(barrierlessCriteria.getUpdatedAt());
        barrierlessCriteriaReadDTO.setBarrierlessCriteriaType(barrierlessCriteria.getBarrierlessCriteriaType().getId());
        barrierlessCriteriaReadDTO.setBarrierlessCriteriaRank(barrierlessCriteria.getBarrierlessCriteriaRank());

        return barrierlessCriteriaReadDTO;
    }
}