package com.hackathon.backend.locationsservice.DTOs.Mappers.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.BarrierlessCriteriaScope.BarrierlessCriteriaCheckCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.BarrierlessCriteriaScope.BarrierlessCriteriaCheckReadDTO;
import com.hackathon.backend.locationsservice.DTOs.Mappers.Base.BaseMapper;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaCheck;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaCheckEmbeddedId;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaRepository;
import com.hackathon.backend.locationsservice.Repositories.LocationScope.LocationRepository;
import com.hackathon.backend.locationsservice.Security.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BarrierlessCriteriaCheckMapper implements BaseMapper<BarrierlessCriteriaCheck, BarrierlessCriteriaCheckReadDTO,BarrierlessCriteriaCheckCreateDTO> {

    private final BarrierlessCriteriaRepository barrierlessCriteriaRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    @Override
    public BarrierlessCriteriaCheck toEntity(BarrierlessCriteriaCheckCreateDTO dto) {

        BarrierlessCriteriaCheck barrierlessCriteriaCheck = new BarrierlessCriteriaCheck();

        barrierlessCriteriaCheck.setUser(userRepository.findById(dto.userId).get());
        barrierlessCriteriaCheck.setLocation(locationRepository.findById(dto.locationId).get());
        barrierlessCriteriaCheck.setBarrierlessCriteria(barrierlessCriteriaRepository.findById(dto.barrierlessCriteriaId).get());
        barrierlessCriteriaCheck.setComment(dto.getComment());
        barrierlessCriteriaCheck.setCreatedBy(dto.createdBy);
        barrierlessCriteriaCheck.setCreatedAt(dto.createdAt);
        barrierlessCriteriaCheck.setUpdatedAt(dto.updatedAt);
        barrierlessCriteriaCheck.setBarrierFreeRating(dto.getBarrierFreeRating());
        barrierlessCriteriaCheck.setHasIssue(dto.isHasIssue());
        barrierlessCriteriaCheck.setImageServiceId(dto.getImageServiceId());

        return barrierlessCriteriaCheck;
    }

    @Override
    public BarrierlessCriteriaCheckReadDTO toDto(BarrierlessCriteriaCheck entity) {
        BarrierlessCriteriaCheckReadDTO barrierlessCriteriaCheckReadDTO = new BarrierlessCriteriaCheckReadDTO();
        barrierlessCriteriaCheckReadDTO.setBarrierlessCriteriaId(entity.getBarrierlessCriteria().getId());
        barrierlessCriteriaCheckReadDTO.setUserId(entity.getUser().getId());
        barrierlessCriteriaCheckReadDTO.setLocationId(entity.getLocation().getId());
        barrierlessCriteriaCheckReadDTO.setComment(entity.getComment());
        barrierlessCriteriaCheckReadDTO.setCreatedBy(entity.getCreatedBy());
        barrierlessCriteriaCheckReadDTO.setCreatedAt(entity.getCreatedAt());
        barrierlessCriteriaCheckReadDTO.setUpdatedAt(entity.getUpdatedAt());
        barrierlessCriteriaCheckReadDTO.setBarrierFreeRating(entity.getBarrierFreeRating());
        barrierlessCriteriaCheckReadDTO.setHasIssue(entity.isHasIssue());
        barrierlessCriteriaCheckReadDTO.setImageServiceId(entity.getImageServiceId());

        return barrierlessCriteriaCheckReadDTO;
    }
}
