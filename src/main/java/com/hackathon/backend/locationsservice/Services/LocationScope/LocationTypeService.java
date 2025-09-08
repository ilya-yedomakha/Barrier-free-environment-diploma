package com.hackathon.backend.locationsservice.Services.LocationScope;

import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.LocationType;
import com.hackathon.backend.locationsservice.Repositories.LocationScope.LocationTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocationTypeService {
    private final LocationTypeRepository locationTypeRepository;

    public Optional<LocationType> getLocationTypeById(UUID locationTypeId){
        return locationTypeRepository.findById(locationTypeId);
    }

}
