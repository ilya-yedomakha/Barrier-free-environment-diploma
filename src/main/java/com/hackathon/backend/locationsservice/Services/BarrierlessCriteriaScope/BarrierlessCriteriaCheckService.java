package com.hackathon.backend.locationsservice.Services.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaCheck;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaCheckRepository;
import com.hackathon.backend.locationsservice.Services.BarrierlessCriteriaScope.interfaces.IBarrierlessCriteriaCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BarrierlessCriteriaCheckService implements IBarrierlessCriteriaCheckService {

    private final BarrierlessCriteriaCheckRepository barrierlessCriteriaCheckRepository;

    @Override
    public BarrierlessCriteriaCheck save(BarrierlessCriteriaCheck criteria) {
        return barrierlessCriteriaCheckRepository.save(criteria);
    }

    @Override
    public void delete(BarrierlessCriteriaCheck criteria) {
        barrierlessCriteriaCheckRepository.delete(criteria);
    }

    @Override
    public void delete(UUID criteriaCheckId) {
        barrierlessCriteriaCheckRepository.deleteById(criteriaCheckId);
    }

    @Override
    public Optional<BarrierlessCriteriaCheck> findById(UUID id) {
        return barrierlessCriteriaCheckRepository.findById(id);
    }


    @Override
    public List<BarrierlessCriteriaCheck> findAll() {
        return barrierlessCriteriaCheckRepository.findAll();
    }

    @Override
    public List<BarrierlessCriteriaCheck> findAllBarrierCriteriaCheckByLocationId(UUID locationId) {
        return List.of();
    }

    @Override
    public List<BarrierlessCriteriaCheck> findAllBarrierCriteriaCheckByUserId(UUID userId) {
        return List.of();
    }

    @Override
    public List<BarrierlessCriteriaCheck> findAllBarrierCriteriaCheckByLocationIdAndCriteria(UUID locationId,
                                                                                         UUID BarrierlessCriteriaTypeId) {
      return  barrierlessCriteriaCheckRepository.findAllByBarrierlessCriteria_IdAndLocation_Id(locationId, BarrierlessCriteriaTypeId);
    }
}
