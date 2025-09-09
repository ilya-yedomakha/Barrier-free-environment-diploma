package com.hackathon.backend.locationsservice.Services;

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
