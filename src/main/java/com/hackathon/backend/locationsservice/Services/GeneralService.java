package com.hackathon.backend.locationsservice.Services;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.Base.BaseCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.Mappers.Base.BaseMapper;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.Base.BaseReadDTO;
import com.hackathon.backend.locationsservice.Domain.Core.Base.BaseEntity;
import com.hackathon.backend.locationsservice.Domain.Core.Base.NamedEntity;
import com.hackathon.backend.locationsservice.Result.EntityErrors.EntityError;
import com.hackathon.backend.locationsservice.Result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class GeneralService<Tmapper extends BaseMapper<T,RDTO,CDTO>, RDTO extends BaseReadDTO, CDTO extends BaseCreateDTO, T extends BaseEntity,U extends JpaRepository<T,UUID>> implements IGeneralService<T,RDTO> {

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
    public Result<T, RDTO> getById(UUID entityId) {
        Optional<T> entity = repository.findById(entityId);
        if (entity.isPresent()){
            Result<T, RDTO> res = Result.success();
            res.setEntity(entity.get());
            res.setEntityDTO(mapper.toDto(entity.get()));
            return res;
        } else return Result.failure(EntityError.notFound(type,entityId));
    }

    @Override
    public Result<T, RDTO> getAll() {
        List<T> entities = repository.findAll();
        Result<T, RDTO> res = Result.success();
        res.setEntities(entities);
        res.entityDTOs = entities.stream().map(mapper::toDto).toList();
        return res;
    }

    protected boolean checkNameDuplicates(List<? extends NamedEntity> entities, String name) {
        String normalizedName = name.toLowerCase().replace(" ", "");

        return entities.stream()
                .map(e -> e.getName().toLowerCase().replace(" ", ""))
                .anyMatch(n -> n.equals(normalizedName));
    }
}
