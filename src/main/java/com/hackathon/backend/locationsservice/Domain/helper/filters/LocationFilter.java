package com.hackathon.backend.locationsservice.Domain.helper.filters;

import com.hackathon.backend.locationsservice.Domain.Enums.LocationStatusEnum;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LocationFilter {
    Double lat;
    Double lng;
    Integer radius;
    String types;
    String features;
    Integer minScore;
    LocationStatusEnum status;
    Boolean verified;
    String query;
    Integer page;
    Integer limit;

}

