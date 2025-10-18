package com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BarrierlessCriteriaGroupRepository extends JpaRepository<BarrierlessCriteriaGroup, UUID> {
    List<BarrierlessCriteriaGroup> findAllByName(String name);

}
