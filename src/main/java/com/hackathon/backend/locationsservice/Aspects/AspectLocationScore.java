package com.hackathon.backend.locationsservice.Aspects;

import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaCheck;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.additional.LocationScoreChg;
import com.hackathon.backend.locationsservice.Repositories.LocationScope.LocationScoreChgRepository;
import com.hackathon.backend.locationsservice.Result.Result;
import lombok.AllArgsConstructor;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
@AllArgsConstructor
public class AspectLocationScore {

    private final LocationScoreChgRepository locationScoreChgRepository;
    private final Logger log = LoggerFactory.getLogger(AspectLocationScore.class);

    @Pointcut("execution(public * com.hackathon.backend.locationsservice.Services.BarrierlessCriteriaScope." +
            "BarrierlessCriteriaCheckService.add(..))")
    public void addMethod() {
    }

    @Pointcut("execution(public void com.hackathon.backend.locationsservice.Services.BarrierlessCriteriaScope." +
            "BarrierlessCriteriaCheckService.delete(com.hackathon.backend.locationsservice." +
            "Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaCheck)) && args(criteria)")
    public void deleteByObject(BarrierlessCriteriaCheck criteria) {}

    @Pointcut(value = "execution(public void com.hackathon.backend.locationsservice.Services.BarrierlessCriteriaScope." +
            "BarrierlessCriteriaCheckService.delete(java.util.UUID, java.util.UUID, java.util.UUID)) && args(locationId, barrierlessCriteriaId, userId)", argNames = "locationId,barrierlessCriteriaId,userId")
    public void deleteByIds(UUID locationId, UUID barrierlessCriteriaId, UUID userId) {}

    @AfterReturning(pointcut = "addMethod()", returning = "result")
    public void afterAdd(Object result) {

        if (result instanceof Result<?, ?> res) {

            Object entity = res.getEntity();

            if (entity instanceof BarrierlessCriteriaCheck added) {

                UUID id = added.getLocation().getId();

                LocationScoreChg locChg = new LocationScoreChg();
                locChg.setLocationId(id);
                locationScoreChgRepository.save(locChg);

                log.info("LocationScoreChgRepository saved(after add()): " + locChg.getLocationId());
            }
        }
    }

    @After(value = "deleteByObject(criteria)", argNames = "criteria")
    public void afterDeleteByObject(BarrierlessCriteriaCheck criteria) {

        UUID id = criteria.getLocation().getId();
        LocationScoreChg locChg = new LocationScoreChg();
        locChg.setLocationId(id);
        locationScoreChgRepository.save(locChg);

        log.info("LocationScoreChgRepository saved(after delete()): " + locChg.getLocationId());

    }


    @After(value = "deleteByIds(locationId, barrierlessCriteriaId, userId)", argNames = "locationId,barrierlessCriteriaId,userId")
    public void afterDeleteByIds(UUID locationId, UUID barrierlessCriteriaId, UUID userId) {

        LocationScoreChg locChg = new LocationScoreChg();
        locChg.setLocationId(locationId);
        locationScoreChgRepository.save(locChg);
        log.info("LocationScoreChgRepository saved(after delete()): " + locChg.getLocationId());

    }


}
