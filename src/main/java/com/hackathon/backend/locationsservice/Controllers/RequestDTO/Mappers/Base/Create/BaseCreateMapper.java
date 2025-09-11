package com.hackathon.backend.locationsservice.Controllers.RequestDTO.Mappers.Base.Create;

import com.hackathon.backend.locationsservice.Controllers.RequestDTO.Mappers.Base.BaseMapper;

public interface BaseCreateMapper<E, D> extends BaseMapper<E, D> {
    @Override
    E toEntity(D dto);

    @Override
    D toDto(E entity);
}
