package com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaCheck;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaCheckEmbeddedId;
import com.hackathon.backend.locationsservice.Security.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BarrierlessCriteriaCheckRepository
        extends JpaRepository<BarrierlessCriteriaCheck, BarrierlessCriteriaCheckEmbeddedId> {

    List<BarrierlessCriteriaCheck> findAllByBarrierlessCriteria_IdAndLocation_Id(UUID barrierlessCriteriaId,
                                                                                 UUID locationId);

    List<BarrierlessCriteriaCheck> findAllByLocation_Id(UUID locationId);

    List<BarrierlessCriteriaCheck> findAllByBarrierlessCriteria_Id(UUID barrierlessCriteriaId);


    List<BarrierlessCriteriaCheck> findAllByUser_Id(UUID userId);

    UUID user(User user);
}

