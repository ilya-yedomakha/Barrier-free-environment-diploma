package com.hackathon.backend.locationsservice.Services.BarrierlessCriteriaScope.interfaces;

import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaCheck;
import jakarta.persistence.Entity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IGeneralService<T> {

    T save(T entity);
    void delete(T entity);
    void delete(UUID entityId);
    Optional<T> findById(UUID id);
    List<T> getAll();

}
