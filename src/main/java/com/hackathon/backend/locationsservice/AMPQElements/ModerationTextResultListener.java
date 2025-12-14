package com.hackathon.backend.locationsservice.AMPQElements;

import com.hackathon.backend.locationsservice.DTOs.RabbitMQDTOs.text_moderation.ModerationCategory;
import com.hackathon.backend.locationsservice.DTOs.RabbitMQDTOs.text_moderation.ModerationElementType;
import com.hackathon.backend.locationsservice.DTOs.RabbitMQDTOs.text_moderation.ModerationResponse;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaCheck;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.Location;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.additional.LocationPendingCopy;
import com.hackathon.backend.locationsservice.Domain.Enums.LocationStatusEnum;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaCheckRepository;
import com.hackathon.backend.locationsservice.Repositories.LocationScope.LocationPendingCopyRepository;
import com.hackathon.backend.locationsservice.Repositories.LocationScope.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ModerationTextResultListener {

    private final LocationRepository locationRepository;
    private final LocationPendingCopyRepository locationPendingCopyRepository;
    private final BarrierlessCriteriaCheckRepository barrierlessCriteriaCheckRepository;

    @RabbitListener(queues = "${amqp.queue.moderation.response}")
    public void handleModerationResponse(ModerationResponse response) {
        log.info("Received moderation result for [{}]: {}", response.getRequestId(), response.getResult().getCategory());


        switch (response.getElementType()) {
            case LOCATION -> {
                Location location = locationRepository.
                        findById(UUID.fromString(response.getRequestId())).orElse(null);

                if(location != null && location.getStatus() != LocationStatusEnum.published) {

                   switch (response.getResult().getCategory()) {
                       case SAFE -> {
                           location.setStatus(LocationStatusEnum.published);
                       }
                       case UNKNOWN -> {
                           location.setStatus(LocationStatusEnum.pending);
                       }
                       default -> {
                           location.setRejectionReason("Не пройшло перевірку штучним інтелектом");
                           location.setLastVerifiedAt(LocalDateTime.now());
                           location.setLastVerifiedBy(null);
                           location.setStatus(LocationStatusEnum.rejected);
                       }
                   }

                    locationRepository.save(location);
                }

            }
            case CHECK -> {
                List<UUID> uuidList = Arrays.stream(response.getRequestId().split(","))
                        .map(String::trim)
                        .map(UUID::fromString)
                        .toList();

                 BarrierlessCriteriaCheck check = barrierlessCriteriaCheckRepository.
                        findAllByBarrierlessCriteria_IdAndLocation_IdAndUser_Id(
                                uuidList.get(0),uuidList.get(1),uuidList.get(2)).orElse(null);


                if (check != null && response.getResult().getCategory() != ModerationCategory.SAFE &&
                        response.getResult().getCategory() != ModerationCategory.UNKNOWN) {

                    barrierlessCriteriaCheckRepository.delete(check);
                }
            }
            case COMMENT -> {
                //якщо будуть коментарі
            }
            case PENDING -> {
                LocationPendingCopy pendingCopy = locationPendingCopyRepository.
                        findById(Long.valueOf(response.getRequestId())).orElse(null);

                if(pendingCopy != null) {

                    switch (response.getResult().getCategory()) {
                        case SAFE -> {
                            pendingCopy.setStatus(LocationStatusEnum.published);
                        }
                        case UNKNOWN -> {
                            pendingCopy.setStatus(LocationStatusEnum.pending);
                        }
                        default -> {
                            pendingCopy.setRejectionReason("Не пройшло перевірку штучним інтелектом");
                            pendingCopy.setRejectedAt(LocalDateTime.now());
                            pendingCopy.setName("****");
                            pendingCopy.setDescription("****");
                            pendingCopy.setRejectedBy(null);
                            pendingCopy.setStatus(LocationStatusEnum.rejected);
                        }
                    }

                    locationPendingCopyRepository.save(pendingCopy);
                }
            }
            default -> {

            }
        }
    }
}
