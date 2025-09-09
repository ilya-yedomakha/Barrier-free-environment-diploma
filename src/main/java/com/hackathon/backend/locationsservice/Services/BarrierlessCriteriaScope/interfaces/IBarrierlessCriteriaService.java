package com.hackathon.backend.locationsservice.Services.BarrierlessCriteriaScope.interfaces;

import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteria;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaCheck;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IBarrierlessCriteriaService {

    BarrierlessCriteria save(BarrierlessCriteria criteria);
    void delete(BarrierlessCriteria criteria);
    void delete(UUID criteriaId);
    Optional<BarrierlessCriteria> findById(UUID id);
    List<BarrierlessCriteria> getAll();

    List<BarrierlessCriteria> findAllBarrierCriteriaCheckByLocationId(UUID locationId);

    List<BarrierlessCriteria> findAllBarrierCriteriaCheckByUserId(UUID userId);

    List<BarrierlessCriteria> findAllBarrierCriteriaCheckByLocationIdAndType(UUID locationId,
                                                                                  UUID BarrierlessCriteriaTypeId);

}
