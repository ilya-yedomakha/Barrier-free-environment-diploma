package com.hackathon.backend.locationsservice.DTOs.ViewLists;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.Base.BaseRegularReadDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.LocationScope.LocationReadDTO;
import com.hackathon.backend.locationsservice.Domain.JSONB_POJOs.Pagination;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class LocationListViewDTO extends BaseRegularReadDTO {
    public List<LocationReadDTO> locationReadDTOS;
    public Pagination pagination;
}
