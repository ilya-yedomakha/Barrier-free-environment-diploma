package com.hackathon.backend.locationsservice.Services.LocationScope;

import com.hackathon.backend.locationsservice.Controllers.RequestDTO.Mappers.Read.LocationScope.LocationReadMapper;
import com.hackathon.backend.locationsservice.Controllers.RequestDTO.Read.LocationScope.LocationReadDTO;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.Location;
import com.hackathon.backend.locationsservice.Domain.Verification;
import com.hackathon.backend.locationsservice.Repositories.LocationScope.LocationRepository;
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
public class LocationService extends GeneralService<LocationReadMapper, LocationReadDTO, Location, LocationRepository> {

    LocationService(LocationRepository locationRepository, LocationReadMapper locationReadMapper){
        super(locationRepository,Location.class,locationReadMapper);
    }

    @PersistenceContext
    private EntityManager entityManager;

    public List<Location> dynamicSearch(Map<String, Object> params) {

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

        int limit = 20;
        int page = 1;

        if (params.get("limit") != null && params.get("page") != null) {
            limit = params.containsKey("limit") ? ((Number) params.get("limit")).intValue() : 20;
            page = params.containsKey("page") ? ((Number) params.get("page")).intValue() : 1;

        }

        int firstResult = (page - 1) * limit;

        query.setFirstResult(firstResult);
        query.setMaxResults(limit);

        return query.getResultList();
    }


    public Location add(Location location) {

        return repository.save(location);
    }

    public Long getLocationsCount() {
        return repository.count();
    }


}
