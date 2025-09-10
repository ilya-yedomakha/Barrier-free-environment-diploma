package com.hackathon.backend.locationsservice.Controllers.RequestDTO.ViewLists;

import com.hackathon.backend.locationsservice.Controllers.RequestDTO.Read.LocationScope.LocationReadDTO;
import com.hackathon.backend.locationsservice.Domain.JSONB_POJOs.Pagination;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class LocationListViewDTO {
    public List<LocationReadDTO> locationReadDTOS;
    public Pagination pagination;
}
