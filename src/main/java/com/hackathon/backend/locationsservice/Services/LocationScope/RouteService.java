package com.hackathon.backend.locationsservice.Services.LocationScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.LocationScope.RouteReadDTO;
import com.hackathon.backend.locationsservice.DTOs.Mappers.LocationScope.RouteMapper;
import com.hackathon.backend.locationsservice.DTOs.RecordDTOs.BuildRouteRequest;
import com.hackathon.backend.locationsservice.DTOs.RecordDTOs.BuildRouteRequestQgis;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.Route;
import com.hackathon.backend.locationsservice.Repositories.LocationScope.LocationTypeRepository;
import com.hackathon.backend.locationsservice.Repositories.LocationScope.RouteRepository;
import com.hackathon.backend.locationsservice.Result.EntityErrors.RouteError;
import com.hackathon.backend.locationsservice.Result.EntityErrors.UserError;
import com.hackathon.backend.locationsservice.Result.Result;
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

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RouteService{
    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;
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

    public Result<Route, RouteReadDTO> getAll() {
        List<Route> entities = routeRepository.findAll();
        Result<Route, RouteReadDTO> res = Result.success();
        res.setEntities(entities);
        res.entityDTOs = entities.stream().map(routeMapper::toDto).toList();
        return res;
    }

    public Result<Route, RouteReadDTO> getRouteByRouteKey(UUID routeKey) {
        Route entity = routeRepository.findByRouteKey(routeKey);
        if (entity == null){
            return Result.failure(RouteError.notFound(routeKey));
        }
        Result<Route, RouteReadDTO> res = Result.success();
        res.setEntity(entity);
        res.entityDTO = routeMapper.toDto(entity);
        return res;
    }

    public Result<BuildRouteRequestQgis, BuildRouteRequestQgis> convertBuildRequestCoordinates(BuildRouteRequest request) {
        String transformedStart = null;

        if (request.lat() != null && request.lng() != null) {
            try {
                Coordinate input = new Coordinate(request.lng(), request.lat());

                Coordinate target = new Coordinate();
                JTS.transform(input, target, transform4326To5564);

                transformedStart = target.x + "," + target.y;

            } catch (Exception e) {
                return Result.failure(RouteError.coordinateTransformFailed(request.lat(), request.lng()));
            }
        } else {
            return Result.failure(RouteError.coordinateEmpty());
        }

        BuildRouteRequestQgis buildRouteRequestQgis = new BuildRouteRequestQgis(request.height(), transformedStart, request.route_key());
        Result<BuildRouteRequestQgis, BuildRouteRequestQgis> res = Result.success();
        res.setEntity(buildRouteRequestQgis);
        res.entityDTO = buildRouteRequestQgis;
        return res;
    }
}