package com.hackathon.backend.locationsservice.Controllers.RequestDTO.Mappers.Base;

public interface BaseMapper<E, D> {
    D toDto(E entity);
    E toEntity(D dto);
}
