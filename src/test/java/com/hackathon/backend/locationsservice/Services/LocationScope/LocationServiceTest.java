package com.hackathon.backend.locationsservice.Services.LocationScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.LocationScope.LocationCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.LocationScope.LocationReadDTO;
import com.hackathon.backend.locationsservice.DTOs.Mappers.LocationScope.LocationMapper;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.Location;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.LocationType;
import com.hackathon.backend.locationsservice.Domain.JSONB_POJOs.Coordinates;
import com.hackathon.backend.locationsservice.Repositories.LocationScope.LocationRepository;
import com.hackathon.backend.locationsservice.Repositories.LocationScope.LocationTypeRepository;
import com.hackathon.backend.locationsservice.Result.EntityErrors.EntityError;
import com.hackathon.backend.locationsservice.Result.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationRepository repository;

    @Mock
    private LocationTypeRepository locationTypeRepository;

    @Mock
    private LocationMapper mapper;

    @InjectMocks
    private LocationService locationService;

    private UUID typeId;
    private LocationType locationType;

    @BeforeEach
    void setUp() {
        typeId = UUID.randomUUID();
        locationType = new LocationType();
        locationType.setId(typeId);
    }


    @Test
    void isValid_success_whenLocationIsCorrect() {
        // given
        LocationCreateDTO dto = createValidDto();

        when(locationTypeRepository.findById(typeId))
                .thenReturn(Optional.of(locationType));

        Location newLocation = createLocation("Unique name");
        when(mapper.toEntity(dto)).thenReturn(newLocation);
        when(mapper.toDto(newLocation)).thenReturn(new LocationReadDTO());

        when(repository.findAll()).thenReturn(List.of());

        // when
        Result<Location, LocationReadDTO> result = locationService.isValid(dto);

        // then
        assertTrue(result.isSuccess());
        assertNotNull(result.entity);
        assertNotNull(result.entityDTO);
        assertEquals("Unique name", result.entity.getName());
    }


    @Test
    void isValid_failure_whenNameAlreadyExists() {
        // given
        LocationCreateDTO dto = createValidDto();

        when(locationTypeRepository.findById(typeId))
                .thenReturn(Optional.of(locationType));

        Location newLocation = createLocation("Same name");
        when(mapper.toEntity(dto)).thenReturn(newLocation);

        Location existing = createLocation("Same name");
        when(repository.findAll()).thenReturn(List.of(existing));

        // when
        Result<Location, LocationReadDTO> result = locationService.isValid(dto);

        // then
        assertFalse(result.isSuccess());
        assertEquals(
                EntityError.sameName(Location.class, "Same name"),
                result.error
        );
    }


    private LocationCreateDTO createValidDto() {
        LocationCreateDTO dto = new LocationCreateDTO();
        dto.setName("Unique name");
        dto.setType(typeId);
        dto.setAddress("Some address");
        dto.setCoordinates(new Coordinates(31.29, 51.50));
        dto.setCreatedAt(LocalDateTime.now());
        return dto;
    }

    private Location createLocation(String name) {
        Location location = new Location();
        location.setName(name);
        location.setCoordinates(mock(Point.class));
        return location;
    }
}
