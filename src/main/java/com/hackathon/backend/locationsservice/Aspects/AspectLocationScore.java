package com.hackathon.backend.locationsservice.Aspects;

import com.hackathon.backend.locationsservice.AMPQElements.ModerationTextEventPub;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.BarrierlessCriteriaScope.BarrierlessCriteriaCheckCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.RabbitMQDTOs.text_moderation.ModerationElementType;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaCheck;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaCheckEmbeddedId;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.additional.LocationScoreChg;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaCheckRepository;
import com.hackathon.backend.locationsservice.Repositories.LocationScope.LocationScoreChgRepository;
import com.hackathon.backend.locationsservice.Result.Result;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Aspect
@Component
@AllArgsConstructor
public class AspectLocationScore {

    private final LocationScoreChgRepository locationScoreChgRepository;
    private final BarrierlessCriteriaCheckRepository barrierlessCriteriaCheckRepository;
    private final Logger log = LoggerFactory.getLogger(AspectLocationScore.class);
    private final ModerationTextEventPub moderationTextEventPub;

    @Pointcut("execution(public * com.hackathon.backend.locationsservice.Services.BarrierlessCriteriaScope." +
            "BarrierlessCriteriaCheckService.add(..))")
    public void addMethod() {
    }

    @Pointcut("execution(public * com.hackathon.backend.locationsservice.Services.BarrierlessCriteriaScope." +
            "BarrierlessCriteriaCheckService.addAll(..))")
    public void addAllMethod() {
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

                moderateCheck(added);
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


    @Around("addAllMethod()")
    public Object aroundAddAll(ProceedingJoinPoint pjp) throws Throwable {

        Object[] args = pjp.getArgs();

        List<BarrierlessCriteriaCheckCreateDTO> dtos =
                (List<BarrierlessCriteriaCheckCreateDTO>) args[0];

        Map<BarrierlessCriteriaCheckEmbeddedId, String> oldComments = new HashMap<>();

        for (BarrierlessCriteriaCheckCreateDTO dto : dtos) {

            BarrierlessCriteriaCheckEmbeddedId id =
                    new BarrierlessCriteriaCheckEmbeddedId(
                            dto.getLocationId(),
                            dto.getBarrierlessCriteriaId(),
                            dto.getUserId()
                    );

            barrierlessCriteriaCheckRepository.findById(id)
                    .ifPresent(old ->
                            oldComments.put(id, old.getComment())
                    );
        }

        Object result = pjp.proceed();

        if (result instanceof Result<?, ?> res &&
                res.getEntities() instanceof List<?> list) {

            for (Object o : list) {

                if (!(o instanceof BarrierlessCriteriaCheck check)) {
                    continue;
                }

                UUID locationId = check.getLocation().getId();
                LocationScoreChg locChg = new LocationScoreChg();
                locChg.setLocationId(locationId);
                locationScoreChgRepository.save(locChg);

                String oldComment =
                        oldComments.get(check.getBarrierlessCriteriaCheckId());

                if (oldComment == null ||
                        !oldComment.equals(check.getComment())) {

                    moderateCheck(check);
                }
            }
        }

        return result;
    }


    private void moderateCheck(BarrierlessCriteriaCheck added) {
        BarrierlessCriteriaCheck check = added;

        BarrierlessCriteriaCheckEmbeddedId embeddedId = check.getBarrierlessCriteriaCheckId();

        String requestId = String.join(",",
                embeddedId.getBarrierlessCriteriaId().toString(),
                embeddedId.getLocationId().toString(),
                embeddedId.getUserId().toString());

        moderationTextEventPub.sendTextForModeration(requestId, ModerationElementType.CHECK, check.getComment());

        log.info("Check is send for moderation: " + requestId);
    }
}
