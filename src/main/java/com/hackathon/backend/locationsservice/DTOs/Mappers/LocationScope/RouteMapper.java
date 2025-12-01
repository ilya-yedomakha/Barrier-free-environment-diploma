package com.hackathon.backend.locationsservice.DTOs.Mappers.LocationScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.LocationScope.RouteCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.LocationScope.RouteReadDTO;
import com.hackathon.backend.locationsservice.DTOs.Mappers.Base.BaseMapper;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.Route;

import lombok.RequiredArgsConstructor;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class RouteMapper implements BaseMapper<Route, RouteReadDTO, RouteCreateDTO> {

    private MathTransform transform5564To4326;

    @PostConstruct
    public void init() throws FactoryException {
        CoordinateReferenceSystem crs4326 = CRS.decode("EPSG:4326", true);
        CoordinateReferenceSystem crs5564 = CRS.decode("EPSG:5564", true);

        transform5564To4326 = CRS.findMathTransform(crs5564, crs4326, true);
    }

    @Override
    public Route toEntity(RouteCreateDTO dto) {
        return copy(dto);
    }

    private Route copy(RouteCreateDTO dto) {
        Route route = new Route();
        route.setName(dto.name);
        route.setAddress(dto.address);
        route.setDescription(dto.description);
        route.setCreatedBy(dto.createdBy);
        route.setOrganizationId(dto.organizationId);
        route.setStatus(dto.status);
        route.setOverallAccessibilityScore(dto.overallAccessibilityScore);
        route.setCreatedAt(dto.createdAt);
        route.setUpdatedAt(dto.updatedAt);
        route.setLastVerifiedAt(dto.lastVerifiedAt);
        route.setRejectionReason(dto.rejectionReason);
        route.setImageServiceId(dto.imageServiceId);
        route.setCost(dto.cost);
        route.setCost1(dto.cost1);
        route.setEnd(dto.end);
        route.setStart(dto.start);
        route.setId(dto.id);
        route.setId_0(dto.id_0);
        route.setUsername(dto.username);
        route.setUpdatedBy(dto.updatedBy);
        route.setGeom(dto.geom);
        route.setLastVerifiedBy(dto.lastVerifiedBy);
        route.setLocationTypeId(dto.locationTypeId);
        route.setRouteKey(dto.routeKey);
        return route;
    }

    @Override
    public RouteReadDTO toDto(Route route) {
        RouteReadDTO dto = new RouteReadDTO();

        dto.setName(route.getName());
        dto.setAddress(route.getAddress());
        dto.setDescription(route.getDescription());
        dto.setCreatedBy(route.getCreatedBy());
        dto.setOrganizationId(route.getOrganizationId());
        dto.setStatus(route.getStatus());
        dto.setOverallAccessibilityScore(route.getOverallAccessibilityScore());
        dto.setCreatedAt(route.getCreatedAt());
        dto.setUpdatedAt(route.getUpdatedAt());
        dto.setLastVerifiedAt(route.getLastVerifiedAt());
        dto.setRejectionReason(route.getRejectionReason());
        dto.setImageServiceId(route.getImageServiceId());
        dto.setCost(route.getCost());
        dto.setCost1(route.getCost1());
        dto.setEnd(route.getEnd());
        dto.setStart(route.getStart());
        dto.setId(route.getId());
        dto.setId_0(route.getId_0());
        dto.setUsername(route.getUsername());
        dto.setUpdatedBy(route.getUpdatedBy());
        dto.setLastVerifiedBy(route.getLastVerifiedBy());
        dto.setLocationTypeId(route.getLocationTypeId());
        dto.setRouteKey(route.getRouteKey());

        // Встановлюємо geom як є
//        dto.setGeom(route.getGeom());

        // 🎯 ОСНОВНЕ — конвертація LineString → координати EPSG:4326
        try {
            LineString geom = route.getGeom();
            if (geom != null) {
                Coordinate[] coords5564 = geom.getCoordinates();
                double[][] coords4326 = new double[coords5564.length][2];

                for (int i = 0; i < coords5564.length; i++) {
                    Coordinate input = coords5564[i];
                    Coordinate target = new Coordinate();

                    // 5564 → 4326
                    JTS.transform(input, target, transform5564To4326);

                    coords4326[i][0] = target.y;  // lat
                    coords4326[i][1] = target.x;  // lng
                }

                dto.setCoordinates(coords4326);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to transform route geometry", e);
        }

        return dto;
    }
}
