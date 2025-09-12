package com.hackathon.backend.locationsservice.Services;

import com.hackathon.backend.locationsservice.Controllers.RequestDTO.Read.Base.BaseReadDTO;
import com.hackathon.backend.locationsservice.Domain.Core.Base.BaseEntity;
import com.hackathon.backend.locationsservice.Result.Result;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IGeneralService<T extends BaseEntity,TDTO extends BaseReadDTO> {

    T add(T entity);

    void delete(T entity);
    void delete(UUID entityId);
    Result<T, TDTO> getById(UUID id);
    Result<T, TDTO> getAll();

}
