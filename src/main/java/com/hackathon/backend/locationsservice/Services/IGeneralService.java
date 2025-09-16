package com.hackathon.backend.locationsservice.Services;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.Base.BaseReadDTO;
import com.hackathon.backend.locationsservice.Result.Result;

import java.util.UUID;

public interface IGeneralService<T,TDTO extends BaseReadDTO> {

    T add(T entity);

    void delete(T entity);
    void delete(UUID entityId);
    Result<T, TDTO> getById(UUID id);
    Result<T, TDTO> getAll();

}
