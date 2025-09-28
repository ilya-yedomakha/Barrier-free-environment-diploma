package com.hackathon.backend.locationsservice.Services.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.BarrierlessCriteriaScope.BarrierlessCriteriaCheckCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.BarrierlessCriteriaScope.BarrierlessCriteriaCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.BarrierlessCriteriaScope.BarrierlessCriteriaCheckReadDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.BarrierlessCriteriaScope.BarrierlessCriteriaReadDTO;
import com.hackathon.backend.locationsservice.DTOs.Mappers.BarrierlessCriteriaScope.BarrierlessCriteriaCheckMapper;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteria;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaCheck;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaCheckEmbeddedId;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaType;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.Location;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaCheckRepository;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaRepository;
import com.hackathon.backend.locationsservice.Repositories.LocationScope.LocationRepository;
import com.hackathon.backend.locationsservice.Result.EntityErrors.EntityError;
import com.hackathon.backend.locationsservice.Result.Result;
import com.hackathon.backend.locationsservice.Security.Domain.User;
import com.hackathon.backend.locationsservice.Security.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BarrierlessCriteriaCheckService{

    private final BarrierlessCriteriaCheckRepository barrierlessCriteriaCheckRepository;
    private final BarrierlessCriteriaRepository barrierlessCriteriaRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final BarrierlessCriteriaCheckMapper barrierlessCriteriaCheckMapper;


    public Result<BarrierlessCriteriaCheck, BarrierlessCriteriaCheckReadDTO> add(BarrierlessCriteriaCheckCreateDTO barrierlessCriteriaCheckCreateDTO) {
        UUID barrierlessCriteriaId = barrierlessCriteriaCheckCreateDTO.getBarrierlessCriteriaId();
        UUID locationId = barrierlessCriteriaCheckCreateDTO.getLocationId();
        UUID userId = barrierlessCriteriaCheckCreateDTO.getUserId();
        Optional<BarrierlessCriteria> barrierlessCriteria = barrierlessCriteriaRepository.findById(barrierlessCriteriaId);
        Optional<Location> location = locationRepository.findById(locationId);
        Optional<User> user = userRepository.findById(userId);
        if (barrierlessCriteria.isEmpty()) {
            return Result.failure(EntityError.notFound(BarrierlessCriteria.class,barrierlessCriteriaCheckCreateDTO.getBarrierlessCriteriaId()));
        }
        if (location.isEmpty()) {
            return Result.failure(EntityError.notFound(Location.class,barrierlessCriteriaCheckCreateDTO.getLocationId()));
        }
        if (user.isEmpty()) {
            return Result.failure(EntityError.notFound(User.class,barrierlessCriteriaCheckCreateDTO.getUserId()));
        }
        BarrierlessCriteriaCheck newBarrierlessCriteriaCheck = barrierlessCriteriaCheckMapper.toEntity(barrierlessCriteriaCheckCreateDTO);
        if (newBarrierlessCriteriaCheck == null) {
            return Result.failure(EntityError.nullReference(BarrierlessCriteriaCheck.class));
        }
        BarrierlessCriteriaCheckEmbeddedId id = new BarrierlessCriteriaCheckEmbeddedId(locationId,barrierlessCriteriaId,userId);

        newBarrierlessCriteriaCheck.setBarrierlessCriteriaCheckId(id);
        BarrierlessCriteriaCheck savedBarrierlessCriteriaCheck = barrierlessCriteriaCheckRepository.save(newBarrierlessCriteriaCheck);
        Result<BarrierlessCriteriaCheck, BarrierlessCriteriaCheckReadDTO> res = Result.success();
        res.entity = savedBarrierlessCriteriaCheck;
        res.entityDTO = barrierlessCriteriaCheckMapper.toDto(savedBarrierlessCriteriaCheck);

        return res;
    }

    public void delete(BarrierlessCriteriaCheck criteria) {
        barrierlessCriteriaCheckRepository.delete(criteria);
    }

    public void delete(UUID locationId, UUID barrierlessCriteriaId, UUID userId) {
        BarrierlessCriteriaCheckEmbeddedId id =
                new BarrierlessCriteriaCheckEmbeddedId(locationId, barrierlessCriteriaId, userId);
        barrierlessCriteriaCheckRepository.deleteById(id);
    }

    public Optional<BarrierlessCriteriaCheck> findById(UUID locationId, UUID barrierlessCriteriaId, UUID userId) {
        BarrierlessCriteriaCheckEmbeddedId id =
                new BarrierlessCriteriaCheckEmbeddedId(locationId, barrierlessCriteriaId, userId);

        return barrierlessCriteriaCheckRepository.findById(id);
    }


    public List<BarrierlessCriteriaCheck> findAll() {
        return barrierlessCriteriaCheckRepository.findAll();
    }

    public List<BarrierlessCriteriaCheckReadDTO> findAllBarrierCriteriaCheckByLocationId(UUID locationId) {

        return barrierlessCriteriaCheckRepository.findAllByLocation_Id(locationId).stream()
                .map(barrierlessCriteriaCheckMapper::toDto).toList();
    }

    public List<BarrierlessCriteriaCheckReadDTO> findAllBarrierCriteriaCheckByUserId(UUID userId) {

        return barrierlessCriteriaCheckRepository.findAllByUser_Id(userId).stream()
                .map(barrierlessCriteriaCheckMapper::toDto).toList();
    }

    public List<BarrierlessCriteriaCheck> findAllBarrierCriteriaCheckByLocationIdAndCriteria(UUID locationId,
                                                                                         UUID BarrierlessCriteriaTypeId) {
      return  barrierlessCriteriaCheckRepository.findAllByBarrierlessCriteria_IdAndLocation_Id(locationId, BarrierlessCriteriaTypeId);
    }

    public List<BarrierlessCriteriaCheckReadDTO> findAllByCriteria_Id(UUID barrierlessCriteriaId) {

        return barrierlessCriteriaCheckRepository.findAllByBarrierlessCriteria_Id(barrierlessCriteriaId).stream()
                .map(barrierlessCriteriaCheckMapper::toDto).toList();
    }
}
