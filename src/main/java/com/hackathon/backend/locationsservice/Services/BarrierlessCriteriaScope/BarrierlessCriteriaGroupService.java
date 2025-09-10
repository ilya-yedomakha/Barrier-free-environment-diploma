package com.hackathon.backend.locationsservice.Services.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaGroup;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaType;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaGroupRepository;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaTypeRepository;
import com.hackathon.backend.locationsservice.Services.GeneralService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class BarrierlessCriteriaGroupService extends GeneralService<BarrierlessCriteriaGroup, BarrierlessCriteriaGroupRepository> {

    BarrierlessCriteriaGroupService(BarrierlessCriteriaGroupRepository repository){
        super(repository);
    }


}
