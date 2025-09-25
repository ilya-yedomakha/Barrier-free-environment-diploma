package com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaCheck;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaCheckEmbeddedId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BarrierlessCriteriaCheckRepository
        extends JpaRepository<BarrierlessCriteriaCheck, BarrierlessCriteriaCheckEmbeddedId> {
    List<BarrierlessCriteriaCheck> findAllByBarrierlessCriteria_IdAndLocation_Id(UUID barrierlessCriteriaId,
                                                                                 UUID locationId);

}

