package com.hackathon.backend.locationsservice.Controllers.RequestDTO;

import com.hackathon.backend.locationsservice.Domain.Enums.FeatureTypeEnum;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class FeatureDTO {
    private FeatureTypeEnum type;
    private String subtype;
    private String description;
    private Boolean status;
    private Integer qualityRating;
    private Boolean standardsCompliance;
//    private UUID createdBy;
//    private LocalDateTime createdAt;
}
