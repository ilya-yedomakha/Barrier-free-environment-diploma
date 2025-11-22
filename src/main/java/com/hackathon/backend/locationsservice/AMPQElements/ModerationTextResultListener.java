package com.hackathon.backend.locationsservice.AMPQElements;

import com.hackathon.backend.locationsservice.DTOs.RabbitMQDTOs.text_moderation.ModerationCategory;
import com.hackathon.backend.locationsservice.DTOs.RabbitMQDTOs.text_moderation.ModerationElementType;
import com.hackathon.backend.locationsservice.DTOs.RabbitMQDTOs.text_moderation.ModerationResponse;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaCheck;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.Location;
import com.hackathon.backend.locationsservice.Domain.Enums.LocationStatusEnum;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaCheckRepository;
import com.hackathon.backend.locationsservice.Repositories.LocationScope.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ModerationTextResultListener {

    private final LocationRepository locationRepository;
    BarrierlessCriteriaCheckRepository barrierlessCriteriaCheckRepository;

    @RabbitListener(queues = "${amqp.queue.moderation.response}")
    public void handleModerationResponse(ModerationResponse response) {
        log.info("Received moderation result for [{}]: {}", response.getRequestId(), response.getResult().getCategory());


        switch (response.getElementType()) {
            case LOCATION -> {
                Location location = locationRepository.
                        findById(UUID.fromString(response.getRequestId())).orElse(null);
                if (location != null && response.getResult().getCategory() != ModerationCategory.SAFE) {
                    location.setStatus(LocationStatusEnum.rejected);
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
            default -> {

            }
        }
    }
}
