package com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BarrierlessCriteriaTypeRepository extends JpaRepository<BarrierlessCriteriaType, UUID> {
}
