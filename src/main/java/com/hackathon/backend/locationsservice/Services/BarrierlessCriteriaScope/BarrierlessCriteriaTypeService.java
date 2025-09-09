package com.hackathon.backend.locationsservice.Services.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaType;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaTypeRepository;
import com.hackathon.backend.locationsservice.Services.GeneralService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class BarrierlessCriteriaTypeService extends GeneralService<BarrierlessCriteriaType, BarrierlessCriteriaTypeRepository> {

    public BarrierlessCriteriaTypeService(BarrierlessCriteriaTypeRepository repository) {
        super(repository);
    }
}
