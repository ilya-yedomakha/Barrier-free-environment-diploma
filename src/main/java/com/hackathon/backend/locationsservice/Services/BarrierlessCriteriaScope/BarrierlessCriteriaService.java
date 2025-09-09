package com.hackathon.backend.locationsservice.Services.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteria;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaType;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaRepository;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaTypeRepository;
import com.hackathon.backend.locationsservice.Services.GeneralService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class BarrierlessCriteriaService extends GeneralService<BarrierlessCriteriaType, BarrierlessCriteriaTypeRepository> {

    BarrierlessCriteriaService(BarrierlessCriteriaTypeRepository repository) {
        super(repository);
    }
}
