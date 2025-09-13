package com.hackathon.backend.locationsservice.DTOs.Mappers.Base;

public interface BaseMapper<E, D> {
    D toDto(E entity);
    E toEntity(D dto);
}
