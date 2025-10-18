package com.hackathon.backend.locationsservice.DTOs.Mappers.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.BarrierlessCriteriaScope.BarrierlessCriteriaTypeCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.BarrierlessCriteriaScope.BarrierlessCriteriaTypeReadDTO;
import com.hackathon.backend.locationsservice.DTOs.Mappers.Base.BaseMapper;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaType;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BarrierlessCriteriaTypeMapper implements BaseMapper<BarrierlessCriteriaType, BarrierlessCriteriaTypeReadDTO, BarrierlessCriteriaTypeCreateDTO> {

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
}
