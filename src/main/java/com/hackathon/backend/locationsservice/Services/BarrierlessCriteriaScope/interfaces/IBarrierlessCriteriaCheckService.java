package com.hackathon.backend.locationsservice.Services.BarrierlessCriteriaScope.interfaces;

import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaCheck;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IBarrierlessCriteriaCheckService {

    BarrierlessCriteriaCheck save(BarrierlessCriteriaCheck criteria);
    void delete(BarrierlessCriteriaCheck criteria);
    void delete(UUID criteriaId);
    Optional<BarrierlessCriteriaCheck> findById(UUID id);
    List<BarrierlessCriteriaCheck> findAll();

    List<BarrierlessCriteriaCheck> findAllBarrierCriteriaCheckByLocationId(UUID locationId);

    List<BarrierlessCriteriaCheck> findAllBarrierCriteriaCheckByUserId(UUID userId);

    List<BarrierlessCriteriaCheck> findAllBarrierCriteriaCheckByLocationIdAndCriteria(UUID locationId,
                                                                                  UUID BarrierlessCriteriaTypeId);

}
