package com.hackathon.backend.locationsservice.Services.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.Controllers.RequestDTO.Mappers.Read.BarrierlessCriteriaScope.BarrierlessCriteriaReadMapper;
import com.hackathon.backend.locationsservice.Controllers.RequestDTO.Read.BarrierlessCriteriaScope.BarrierlessCriteriaReadDTO;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteria;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaRepository;
import com.hackathon.backend.locationsservice.Services.GeneralService;
import org.springframework.stereotype.Service;

@Service
public class BarrierlessCriteriaService extends GeneralService<BarrierlessCriteriaReadMapper,BarrierlessCriteriaReadDTO,BarrierlessCriteria,BarrierlessCriteriaRepository> {

    BarrierlessCriteriaService(BarrierlessCriteriaRepository barrierlessCriteriaRepository, BarrierlessCriteriaReadMapper barrierlessCriteriaReadMapper){
        super(barrierlessCriteriaRepository,BarrierlessCriteria.class,barrierlessCriteriaReadMapper);
    }
}
