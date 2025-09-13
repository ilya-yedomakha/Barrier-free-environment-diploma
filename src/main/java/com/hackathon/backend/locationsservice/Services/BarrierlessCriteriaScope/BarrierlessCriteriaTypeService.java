package com.hackathon.backend.locationsservice.Services.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.DTOs.Mappers.Read.BarrierlessCriteriaScope.BarrierlessCriteriaTypeReadMapper;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.BarrierlessCriteriaScope.BarrierlessCriteriaTypeReadDTO;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaType;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaTypeRepository;
import com.hackathon.backend.locationsservice.Services.GeneralService;
import org.springframework.stereotype.Service;

@Service
public class BarrierlessCriteriaTypeService extends GeneralService<BarrierlessCriteriaTypeReadMapper, BarrierlessCriteriaTypeReadDTO, BarrierlessCriteriaType, BarrierlessCriteriaTypeRepository> {

    BarrierlessCriteriaTypeService(BarrierlessCriteriaTypeRepository barrierlessCriteriaTypeRepository, BarrierlessCriteriaTypeReadMapper barrierlessCriteriaTypeReadMapper){
        super(barrierlessCriteriaTypeRepository, BarrierlessCriteriaType.class,barrierlessCriteriaTypeReadMapper);
    }


}
