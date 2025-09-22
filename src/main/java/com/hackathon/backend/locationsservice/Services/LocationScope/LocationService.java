package com.hackathon.backend.locationsservice.Services.LocationScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.LocationScope.LocationCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.LocationScope.LocationReadDTO;
import com.hackathon.backend.locationsservice.DTOs.Mappers.LocationScope.LocationMapper;
import com.hackathon.backend.locationsservice.DTOs.ViewLists.LocationListViewDTO;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaGroup;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.Location;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.LocationType;
import com.hackathon.backend.locationsservice.Domain.JSONB_POJOs.Pagination;
import com.hackathon.backend.locationsservice.Domain.Verification;
import com.hackathon.backend.locationsservice.Repositories.LocationScope.LocationRepository;
import com.hackathon.backend.locationsservice.Repositories.LocationScope.LocationTypeRepository;
import com.hackathon.backend.locationsservice.Result.EntityErrors.EntityError;
import com.hackathon.backend.locationsservice.Result.EntityErrors.LocationError;
import com.hackathon.backend.locationsservice.Result.Result;
import com.hackathon.backend.locationsservice.Services.GeneralService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LocationService extends GeneralService<LocationMapper, LocationReadDTO, LocationCreateDTO, Location, LocationRepository> {

    private final LocationTypeRepository locationTypeRepository;

    LocationService(LocationRepository locationRepository, LocationMapper locationMapper, LocationTypeRepository locationTypeRepository) {
        super(locationRepository, Location.class, locationMapper);
        this.locationTypeRepository = locationTypeRepository;
    }

    @PersistenceContext
    private EntityManager entityManager;


    public Result<Location, LocationListViewDTO> getAll(Map<String, Object> params) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Location> cq = cb.createQuery(Location.class);
        Root<Location> locationRoot = cq.from(Location.class);

        List<Predicate> predicates = new ArrayList<>();

        if (params.containsKey("status")) {
            predicates.add(cb.equal(locationRoot.get("status"), params.get("status")));
        }

        if (params.containsKey("types")) {
            String[] types = ((String) params.get("types")).split(",");
            predicates.add(locationRoot.get("type").in(Arrays.asList(types)));
        }

        if (params.containsKey("query")) {
            String query = ((String) params.get("query")).toLowerCase();
            String pattern = "%" + query + "%";
            predicates.add(
                    cb.or(
                            cb.like(cb.lower(locationRoot.get("name")), pattern),
                            cb.like(cb.lower(locationRoot.get("address")), pattern)
                    )
            );
        }

//TODO:  CHANGE TO FIND BY MIN OVERALL ACCESSIBILITY SCORE

//        if (params.containsKey("minScore")) {
//            Integer minScore = (Integer) params.get("minScore");
//
//            Subquery<UUID> subquery = cq.subquery(UUID.class);
//            Root<Feature> featureRoot = subquery.from(Feature.class);
//            subquery.select(featureRoot.get("locationId"))
//                    .where(cb.greaterThanOrEqualTo(featureRoot.get("qualityRating"), minScore));
//
//            predicates.add(locationRoot.get("id").in(subquery));
//        }
//
//        if (params.containsKey("features")) {
//            String[] features = ((String) params.get("features")).split(",");
//            Subquery<UUID> featureSub = cq.subquery(UUID.class);
//            Root<Feature> featureRoot = featureSub.from(Feature.class);
//            featureSub.select(featureRoot.get("locationId"))
//                    .where(featureRoot.get("type").in(Arrays.asList(features)));
//
//            predicates.add(locationRoot.get("id").in(featureSub));
//        }

        if (params.containsKey("verified") && Boolean.TRUE.equals(params.get("verified"))) {
            Subquery<UUID> verificationSub = cq.subquery(UUID.class);
            Root<Verification> verRoot = verificationSub.from(Verification.class);
            verificationSub.select(verRoot.get("locationId"))
                    .where(cb.isTrue(verRoot.get("status")));

            predicates.add(locationRoot.get("id").in(verificationSub));
        }

        if (params.containsKey("lat") && params.containsKey("lng")) {
            double lat = (Double) params.get("lat");
            double lng = (Double) params.get("lng");
            double radius = params.containsKey("radius") ? ((Number) params.get("radius")).doubleValue() : 5000.0;

            GeometryFactory geometryFactory = new GeometryFactory();
            Point point = geometryFactory.createPoint(new Coordinate(lng, lat));
            point.setSRID(4326);

            Expression<Double> distanceExpr = cb.function("ST_Distance", Double.class,
                    locationRoot.get("coordinates"), cb.literal(point));

            predicates.add(cb.lessThanOrEqualTo(distanceExpr, radius));
        }

        cq.select(locationRoot).where(cb.and(predicates.toArray(new Predicate[0])));

        TypedQuery<Location> query = entityManager.createQuery(cq);

        Long countLocations = getLocationsCount();

        int limit = 20;
        int page = 1;

        if (params.get("limit") != null) {
            int paramLimit = ((Number) params.get("limit")).intValue();
            limit = paramLimit > 0 ? paramLimit : 20;
        }

        if (params.get("page") != null) {
            int paramPage = ((Number) params.get("page")).intValue();
            page = paramPage > 0 ? paramPage : 1;
        }


        int firstResult = (page - 1) * limit;

        query.setFirstResult(firstResult);
        query.setMaxResults(limit);


        Pagination pagination;
        pagination = new Pagination(page, limit, countLocations, countLocations / limit);

        List<Location> entities = query.getResultList();
        Result<Location, LocationListViewDTO> res = Result.success();
        res.setEntities(query.getResultList());
        res.entityDTO = new LocationListViewDTO(entities.stream().map(mapper::toDto).toList(), pagination);
        return res;
    }


    public Result<Location, LocationReadDTO> add(LocationCreateDTO locationCreateDTO) {
        Optional<LocationType> locationType = locationTypeRepository.findById(locationCreateDTO.getType());
        if (locationType.isEmpty()) {
            return Result.failure(EntityError.notFound(BarrierlessCriteriaGroup.class,locationCreateDTO.getType()));
        }
        Location newLocation = mapper.toEntity(locationCreateDTO);
        if (newLocation == null) {
            return Result.failure(EntityError.nullReference(type));
        }
        List<Location> locations = repository.findAll();
        if(checkNameDuplicates(locations,newLocation.getName())){
            return Result.failure(EntityError.sameName(type, newLocation.getName()));
        };

        //TODO: There can be same descriptions for different locations?
//        List<Location> locationDescriptionDuplicates = repository.findAllByDescription(newLocation.getDescription());
//        if (locations != null && !locations.isEmpty()) {
//            return Result.failure(EntityError.sameDesc(type, newLocation.getDescription()));
//        }

        //TODO: Maybe in some radius
        for (Location location : locations) {
            if (location.getCoordinates().equals(newLocation.getCoordinates())) {
                return Result.failure(LocationError.sameCoordinates(newLocation.getCoordinates()));
            }
        }

        //TODO: There can be same addresses for different locations?
//        List<Location> locationAddressDuplicates = repository.findAllByDescription(newLocation.getDescription());
//        if (locations != null && !locations.isEmpty()) {
//            return Result.failure(EntityError.sameName(type, newLocation.getDescription()));
//        }

        Location savedLocation = repository.save(newLocation);
        Result<Location, LocationReadDTO> res = Result.success();
        res.entity = savedLocation;
        res.entityDTO = mapper.toDto(savedLocation);

        return res;

    }

    public Long getLocationsCount() {
        return repository.count();
    }


}
