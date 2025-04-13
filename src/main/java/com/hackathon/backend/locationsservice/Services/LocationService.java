package com.hackathon.backend.locationsservice.Services;

import com.hackathon.backend.locationsservice.Domain.Enums.LocationStatusEnum;
import com.hackathon.backend.locationsservice.Domain.Enums.LocationTypeEnum;
import com.hackathon.backend.locationsservice.Domain.Location;
import com.hackathon.backend.locationsservice.Domain.helper.filters.LocationFilter;
import com.hackathon.backend.locationsservice.Repositories.LocationRepository;
import com.hackathon.backend.locationsservice.exceptions.ValidationFilterException;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public Optional<Location> getById(UUID locationId){
        return locationRepository.findById(locationId);
    }

    public Location add(Location location){

        return locationRepository.save(location);
    }

    private final String  locationTypes= Arrays.stream(LocationTypeEnum.values())
            .map(Enum::name)
            .collect(Collectors.joining(" "));

//    public List<Location> getAllLocations(){
//
//        return locationRepository.findAll();
//    }

    public List<Location> getAllLocations(LocationFilter locationFilter) throws ValidationFilterException{

        if ((locationFilter.getLat() != null && locationFilter.getLng() == null) ||
                (locationFilter.getLat() == null && locationFilter.getLng() != null)) {
            throw  new ValidationFilterException("Both longitude and latitude must be provided!");
        }
        if ((locationFilter.getLng() != null && locationFilter.getLat() != null) &&
                locationFilter.getRadius() == null){
            locationFilter.setRadius(5000);
        }
        if (locationFilter.getTypes().isBlank()){
            locationFilter.setTypes(null);
        }


        String[] parts = locationFilter.getTypes().split(", ");

        for (String part : parts) {
            if(!locationTypes.contains(part)){
                throw  new ValidationFilterException("Incorrect location type: !" + part);
            }
        }
        return locationRepository.findAll();
    }

    public LocationFilter validateLocationFilter(LocationFilter locationFilter) throws ValidationFilterException {
        if ((locationFilter.getLat() != null && locationFilter.getLng() == null) ||
                (locationFilter.getLat() == null && locationFilter.getLng() != null)) {
            throw  new ValidationFilterException("Both longitude and latitude must be provided!");
        }
        if ((locationFilter.getLng() != null && locationFilter.getLat() != null) &&
                locationFilter.getRadius() == null){
            locationFilter.setRadius(5000);
        }
            if (locationFilter.getTypes().isBlank()){
                locationFilter.setTypes(null);
            }


            String[] parts = locationFilter.getTypes().split(", ");

            for (String part : parts) {
                if(!locationTypes.contains(part)){
                    throw  new ValidationFilterException("Incorrect location type: !" + part);
                }
            }
        return locationFilter;
    }
}
