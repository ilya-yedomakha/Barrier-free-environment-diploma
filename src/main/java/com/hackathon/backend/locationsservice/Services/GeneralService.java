package com.hackathon.backend.locationsservice.Services;

import com.hackathon.backend.locationsservice.Controllers.RequestDTO.Mappers.Base.BaseMapper;
import com.hackathon.backend.locationsservice.Controllers.RequestDTO.Read.Base.BaseReadDTO;
import com.hackathon.backend.locationsservice.Domain.Core.Base.BaseEntity;
import com.hackathon.backend.locationsservice.Result.EntityErrors.EntityError;
import com.hackathon.backend.locationsservice.Result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class GeneralService<Tmapper extends BaseMapper<T,TDTO>, TDTO extends BaseReadDTO, T extends BaseEntity,U extends JpaRepository<T,UUID>> implements IGeneralService<T,TDTO> {

    protected final U repository;
    protected final Class<T> type;
    protected final Tmapper mapper;

    @Override
    public T add(T entity) {
        return repository.save(entity);
    }

    @Override
    public void delete(T entity) {
        repository.delete(entity);
    }

    @Override
    public void delete(UUID entityId) {
        repository.deleteById(entityId);
    }

    @Override
    public Result<T, TDTO> getById(UUID entityId) {
        Optional<T> entity = repository.findById(entityId);
        if (entity.isPresent()){
            Result<T, TDTO> res = Result.success();
            res.setEntity(entity.get());
            res.setEntityDTO(mapper.toDto(entity.get()));
            return res;
        } else return Result.failure(EntityError.notFound(type,entityId));
    }

    @Override
    public Result<T, TDTO> getAll() {
        List<T> entities = repository.findAll();
        Result<T, TDTO> res = Result.success();
        res.setEntities(entities);
        res.entityDTOs = entities.stream().map(mapper::toDto).toList();
        return res;
    }
}
