package com.hackathon.backend.locationsservice.DTOs.Mappers.Base.Read;

import com.hackathon.backend.locationsservice.DTOs.Mappers.Base.BaseMapper;

public interface BaseReadMapper<E, D> extends BaseMapper<E, D> {
    @Override
    D toDto(E entity);

    @Override
    E toEntity(D dto);
}
