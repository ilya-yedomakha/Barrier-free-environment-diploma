package com.hackathon.backend.locationsservice.DTOs;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.LocationScope.LocationReadDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SimilarLocationDTO {
    private LocationReadDTO location;
    private double likeness;
}

