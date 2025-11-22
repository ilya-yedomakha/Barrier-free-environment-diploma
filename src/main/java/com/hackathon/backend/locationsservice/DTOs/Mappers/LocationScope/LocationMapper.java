package com.hackathon.backend.locationsservice.DTOs.Mappers.LocationScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.LocationScope.LocationCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.LocationScope.LocationReadDTO;
import com.hackathon.backend.locationsservice.DTOs.Mappers.Base.BaseMapper;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.Location;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.LocationType;
import com.hackathon.backend.locationsservice.Domain.JSONB_POJOs.Coordinates;
import com.hackathon.backend.locationsservice.Repositories.LocationScope.LocationTypeRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationMapper implements BaseMapper<Location, LocationReadDTO, LocationCreateDTO> {

    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 5564);
    private final LocationTypeRepository locationTypeRepository;

    private MathTransform transform4326To5564;
    private MathTransform transform5564To4326;

    @PostConstruct
    public void init() throws FactoryException {
        CoordinateReferenceSystem crs4326 = CRS.decode("EPSG:4326", true);
        CoordinateReferenceSystem crs5564 = CRS.decode("EPSG:5564", true);

        transform4326To5564 = CRS.findMathTransform(crs4326, crs5564, true);
        transform5564To4326 = CRS.findMathTransform(crs5564, crs4326, true);
    }

    @Override
    public Location toEntity(LocationCreateDTO dto) {
        Point coordinates = null;

        if (dto.coordinates != null) {
            try {
                // 1️⃣ Координати з фронту: 4326
                Coordinate input = new Coordinate(dto.coordinates.getLng(), dto.coordinates.getLat());

                // 2️⃣ Переводимо 4326 → 5564
                Coordinate target = new Coordinate();
                JTS.transform(input, target, transform4326To5564);

                coordinates = geometryFactory.createPoint(target);

            } catch (Exception e) {
                throw new RuntimeException("Coordinate transform 4326→5564 failed", e);
            }
        }

        Location location = new Location();
        location.setName(dto.name);
        location.setAddress(dto.address);
        location.setCoordinates(coordinates);
        LocationType locationType = locationTypeRepository.findById(dto.type).get();
        location.setType(locationType);
        location.setDescription(dto.description);
        location.setContacts(dto.contacts);
        location.setWorkingHours(dto.workingHours);
        location.setCreatedBy(dto.createdBy);
        location.setOrganizationId(dto.organizationId);
        location.setStatus(dto.status);
        location.setOverallAccessibilityScore(dto.overallAccessibilityScore);
        location.setCreatedAt(dto.createdAt);
        location.setUpdatedAt(dto.updatedAt);
        location.setLastVerifiedAt(dto.lastVerifiedAt);
        location.setRejectionReason(dto.rejectionReason);
        location.setImageServiceId(dto.getImageServiceId());

        return location;
    }

    @Override
    public LocationReadDTO toDto(Location location) {
        LocationReadDTO dto = new LocationReadDTO();

        dto.setId(location.getId());
        dto.setName(location.getName());
        dto.setAddress(location.getAddress());

        try {
            // 1️⃣ Координати в базі: 5564
            Coordinate input = new Coordinate(
                    location.getCoordinates().getX(),
                    location.getCoordinates().getY()
            );

            // 2️⃣ Переводимо 5564 → 4326 щоб підходило для Leaflet
            Coordinate target = new Coordinate();
            JTS.transform(input, target, transform5564To4326);

            dto.setCoordinates(new Coordinates(target.x, target.y));

        } catch (Exception e) {
            throw new RuntimeException("Coordinate transform 5564→4326 failed", e);
        }

        dto.setType(location.getType().getId());
        dto.setDescription(location.getDescription());
        dto.setContacts(location.getContacts());
        dto.setWorkingHours(location.getWorkingHours());
        dto.setCreatedBy(location.getCreatedBy());
        dto.setOrganizationId(location.getOrganizationId());
        dto.setStatus(location.getStatus());
        dto.setOverallAccessibilityScore(location.getOverallAccessibilityScore());
        dto.setCreatedAt(location.getCreatedAt());
        dto.setUpdatedAt(location.getUpdatedAt());
        dto.setLastVerifiedAt(location.getLastVerifiedAt());
        dto.setRejectionReason(location.getRejectionReason());
        dto.setImageServiceId(location.getImageServiceId());

        return dto;
    }
}

