package com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BarrierlessCriteriaRepository extends JpaRepository<BarrierlessCriteria, UUID> {
}
