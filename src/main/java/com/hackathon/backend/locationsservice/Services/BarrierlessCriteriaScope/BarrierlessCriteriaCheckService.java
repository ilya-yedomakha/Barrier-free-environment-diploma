package com.hackathon.backend.locationsservice.Services.BarrierlessCriteriaScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.BarrierlessCriteriaScope.BarrierlessCriteriaCheckCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.BarrierlessCriteriaScope.BarrierlessCriteriaCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.BarrierlessCriteriaScope.BarrierlessCriteriaCheckReadDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.BarrierlessCriteriaScope.BarrierlessCriteriaReadDTO;
import com.hackathon.backend.locationsservice.DTOs.Mappers.BarrierlessCriteriaScope.BarrierlessCriteriaCheckMapper;
import com.hackathon.backend.locationsservice.DTOs.ViewLists.LocationListViewDTO;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.*;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.Location;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.LocationType;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaCheckRepository;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaRepository;
import com.hackathon.backend.locationsservice.Repositories.LocationScope.LocationRepository;
import com.hackathon.backend.locationsservice.Result.EntityErrors.CheckError;
import com.hackathon.backend.locationsservice.Result.EntityErrors.EntityError;
import com.hackathon.backend.locationsservice.Result.Result;
import com.hackathon.backend.locationsservice.Security.Domain.User;
import com.hackathon.backend.locationsservice.Security.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

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
        Optional<BarrierlessCriteria> barrierlessCriteriaOptional = barrierlessCriteriaRepository.findById(barrierlessCriteriaId);
        Optional<Location> locationOptional = locationRepository.findById(locationId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (barrierlessCriteriaOptional.isEmpty()) {
            return Result.failure(EntityError.notFound(BarrierlessCriteria.class,barrierlessCriteriaCheckCreateDTO.getBarrierlessCriteriaId()));
        }
        if (locationOptional.isEmpty()) {
            return Result.failure(EntityError.notFound(Location.class,barrierlessCriteriaCheckCreateDTO.getLocationId()));
        }
        if (userOptional.isEmpty()) {
            return Result.failure(EntityError.notFound(User.class,barrierlessCriteriaCheckCreateDTO.getUserId()));
        }
        BarrierlessCriteriaCheck newBarrierlessCriteriaCheck = barrierlessCriteriaCheckMapper.toEntity(barrierlessCriteriaCheckCreateDTO);
        if (newBarrierlessCriteriaCheck == null) {
            return Result.failure(EntityError.nullReference(BarrierlessCriteriaCheck.class));
        }
        BarrierlessCriteria barrierlessCriteria = barrierlessCriteriaOptional.get();
        Location location = locationOptional.get();
        LocationType locationType = location.getType();
        Set<LocationType> criteriaLocationTypes = barrierlessCriteria.getBarrierlessCriteriaType().getBarrierlessCriteriaGroup().getLocationTypes();
        if (!criteriaLocationTypes.contains(locationType)){
            return Result.failure(CheckError.mismatch(location.getType().getId(),barrierlessCriteriaId));
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

    public Result<BarrierlessCriteriaCheck, BarrierlessCriteriaCheckReadDTO> addAll(List<BarrierlessCriteriaCheckCreateDTO> checkList) {

        List<BarrierlessCriteriaCheck> barrierlessCriteriaChecks = new ArrayList<>();
        for (BarrierlessCriteriaCheckCreateDTO barrierlessCriteriaCheckCreateDTO : checkList){
            UUID barrierlessCriteriaId = barrierlessCriteriaCheckCreateDTO.getBarrierlessCriteriaId();
            UUID locationId = barrierlessCriteriaCheckCreateDTO.getLocationId();
            UUID userId = barrierlessCriteriaCheckCreateDTO.getUserId();
            Optional<BarrierlessCriteria> barrierlessCriteriaOptional = barrierlessCriteriaRepository.findById(barrierlessCriteriaId);
            Optional<Location> locationOptional = locationRepository.findById(locationId);
            Optional<User> userOptional = userRepository.findById(userId);

            if (barrierlessCriteriaOptional.isEmpty()) {
                return Result.failure(EntityError.notFound(BarrierlessCriteria.class,barrierlessCriteriaCheckCreateDTO.getBarrierlessCriteriaId()));
            }
            if (locationOptional.isEmpty()) {
                return Result.failure(EntityError.notFound(Location.class,barrierlessCriteriaCheckCreateDTO.getLocationId()));
            }
            if (userOptional.isEmpty()) {
                return Result.failure(EntityError.notFound(User.class,barrierlessCriteriaCheckCreateDTO.getUserId()));
            }
            BarrierlessCriteriaCheck newBarrierlessCriteriaCheck = barrierlessCriteriaCheckMapper.toEntity(barrierlessCriteriaCheckCreateDTO);
            if (newBarrierlessCriteriaCheck == null) {
                return Result.failure(EntityError.nullReference(BarrierlessCriteriaCheck.class));
            }
            BarrierlessCriteria barrierlessCriteria = barrierlessCriteriaOptional.get();
            Location location = locationOptional.get();
            LocationType locationType = location.getType();
            Set<LocationType> criteriaLocationTypes = barrierlessCriteria.getBarrierlessCriteriaType().getBarrierlessCriteriaGroup().getLocationTypes();
            if (!criteriaLocationTypes.contains(locationType)){
                return Result.failure(CheckError.mismatch(location.getType().getId(),barrierlessCriteriaId));
            }
            BarrierlessCriteriaCheckEmbeddedId id = new BarrierlessCriteriaCheckEmbeddedId(locationId,barrierlessCriteriaId,userId);

            newBarrierlessCriteriaCheck.setBarrierlessCriteriaCheckId(id);
            barrierlessCriteriaChecks.add(newBarrierlessCriteriaCheck);
        }


        List<BarrierlessCriteriaCheck> savedBarrierlessCriteriaChecks = barrierlessCriteriaCheckRepository.saveAll(barrierlessCriteriaChecks);
        Result<BarrierlessCriteriaCheck, BarrierlessCriteriaCheckReadDTO> res = Result.success();
        res.entities = savedBarrierlessCriteriaChecks;
        res.entityDTOs = barrierlessCriteriaChecks.stream().map(barrierlessCriteriaCheckMapper::toDto).toList();

        return res;
    }
}
