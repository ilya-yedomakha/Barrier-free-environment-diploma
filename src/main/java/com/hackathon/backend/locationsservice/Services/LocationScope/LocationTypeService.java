package com.hackathon.backend.locationsservice.Services.LocationScope;

import com.hackathon.backend.locationsservice.DTOs.Mappers.Create.LocationScope.LocationTypeCreateMapper;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.LocationScope.LocationTypeReadDTO;
import com.hackathon.backend.locationsservice.DTOs.Mappers.Read.LocationScope.LocationTypeReadMapper;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.LocationType;
import com.hackathon.backend.locationsservice.Repositories.LocationScope.LocationTypeRepository;
import com.hackathon.backend.locationsservice.Services.GeneralService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class LocationTypeService extends GeneralService<LocationTypeReadMapper, LocationTypeReadDTO, LocationType, LocationTypeRepository> {
    private final LocationTypeCreateMapper locationTypeCreateMapper;

    LocationTypeService(LocationTypeRepository locationTypeRepository, LocationTypeReadMapper locationTypeReadMapper, LocationTypeCreateMapper locationTypeCreateMapper) {
        super(locationTypeRepository, LocationType.class, locationTypeReadMapper);
        this.locationTypeCreateMapper = locationTypeCreateMapper;
    }

}