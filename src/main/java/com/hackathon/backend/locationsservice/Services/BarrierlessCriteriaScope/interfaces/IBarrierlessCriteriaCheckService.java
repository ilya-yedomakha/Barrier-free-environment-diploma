//package com.hackathon.backend.locationsservice.Services.BarrierlessCriteriaScope.interfaces;
//
//import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.BarrierlessCriteriaScope.BarrierlessCriteriaCheckReadDTO;
//import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaCheck;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//public interface IBarrierlessCriteriaCheckService {
//
//    BarrierlessCriteriaCheck save(BarrierlessCriteriaCheckReadDTO criteria);
//    void delete(BarrierlessCriteriaCheck criteria);
//    void delete(UUID locationId, UUID barrierlessCriteriaId, UUID userId);
//    Optional<BarrierlessCriteriaCheck> findById(UUID locationId, UUID barrierlessCriteriaId, UUID userId);
//    List<BarrierlessCriteriaCheck> findAll();
//
//    List<BarrierlessCriteriaCheck> findAllBarrierCriteriaCheckByLocationId(UUID locationId);
//
//    List<BarrierlessCriteriaCheck> findAllBarrierCriteriaCheckByUserId(UUID userId);
//
//    List<BarrierlessCriteriaCheck> findAllBarrierCriteriaCheckByLocationIdAndCriteria(UUID locationId,
//                                                                                  UUID BarrierlessCriteriaTypeId);
//
//}
