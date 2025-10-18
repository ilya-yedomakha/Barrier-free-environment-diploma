package com.hackathon.backend.locationsservice.DTOs.Mappers.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.BarrierlessCriteriaScope.BarrierlessCriteriaCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.BarrierlessCriteriaScope.BarrierlessCriteriaReadDTO;
import com.hackathon.backend.locationsservice.DTOs.Mappers.Base.BaseMapper;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteria;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BarrierlessCriteriaMapper implements BaseMapper<BarrierlessCriteria, BarrierlessCriteriaReadDTO,BarrierlessCriteriaCreateDTO> {

    private final BarrierlessCriteriaTypeRepository barrierlessCriteriaTypeRepository;

    @Override
    public BarrierlessCriteria toEntity(BarrierlessCriteriaCreateDTO dto) {

        BarrierlessCriteria barrierlessCriteria = new BarrierlessCriteria();
        barrierlessCriteria.setName(dto.name);
        barrierlessCriteria.setDescription(dto.description);
        barrierlessCriteria.setCreatedBy(dto.createdBy);
        barrierlessCriteria.setCreatedAt(dto.createdAt);
        barrierlessCriteria.setUpdatedAt(dto.updatedAt);
        barrierlessCriteria.setBarrierlessCriteriaRank(dto.barrierlessCriteriaRank);
        barrierlessCriteria.setBarrierlessCriteriaType(barrierlessCriteriaTypeRepository.findById(dto.getBarrierlessCriteriaTypeId()).get());

        return barrierlessCriteria;
    }

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
}
