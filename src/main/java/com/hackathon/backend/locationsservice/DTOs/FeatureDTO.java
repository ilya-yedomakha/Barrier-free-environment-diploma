package com.hackathon.backend.locationsservice.DTOs;

import com.hackathon.backend.locationsservice.Domain.Enums.FeatureTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeatureDTO {
    private FeatureTypeEnum type;
    private String subtype;
    private String description;
    private Boolean status;
    private Integer qualityRating;
    private Boolean standardsCompliance;
}
