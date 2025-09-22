package com.hackathon.backend.locationsservice.DTOs.Mappers.Base;

public interface BaseMapper<E, readD, createD> {
    readD toDto(E entity);
    E toEntity(createD dto);
}
