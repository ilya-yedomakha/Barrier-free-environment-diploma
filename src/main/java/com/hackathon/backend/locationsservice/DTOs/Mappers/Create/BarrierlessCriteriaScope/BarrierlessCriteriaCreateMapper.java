package com.hackathon.backend.locationsservice.DTOs.Mappers.Create.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.BarrierlessCriteriaScope.BarrierlessCriteriaCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.LocationScope.LocationCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.Mappers.Base.Create.BaseCreateMapper;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteria;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.Location;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaRepository;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BarrierlessCriteriaCreateMapper implements BaseCreateMapper<BarrierlessCriteria, BarrierlessCriteriaCreateDTO> {

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
    public BarrierlessCriteriaCreateDTO toDto(BarrierlessCriteria entity) {
        throw new UnsupportedOperationException("Not implemented in CreateMapper");
    }
}
