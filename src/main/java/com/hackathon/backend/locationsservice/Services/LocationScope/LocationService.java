package com.hackathon.backend.locationsservice.Services.LocationScope;

import com.hackathon.backend.locationsservice.Domain.LocationScope.Location;
import com.hackathon.backend.locationsservice.Repositories.LocationScope.LocationRepository;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.hackathon.backend.locationsservice.Domain.Feature;
import com.hackathon.backend.locationsservice.Domain.Verification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.util.*;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

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

        if (params.containsKey("minScore")) {
            Integer minScore = (Integer) params.get("minScore");

            Subquery<UUID> subquery = cq.subquery(UUID.class);
            Root<Feature> featureRoot = subquery.from(Feature.class);
            subquery.select(featureRoot.get("locationId"))
                    .where(cb.greaterThanOrEqualTo(featureRoot.get("qualityRating"), minScore));

            predicates.add(locationRoot.get("id").in(subquery));
        }

        if (params.containsKey("features")) {
            String[] features = ((String) params.get("features")).split(",");
            Subquery<UUID> featureSub = cq.subquery(UUID.class);
            Root<Feature> featureRoot = featureSub.from(Feature.class);
            featureSub.select(featureRoot.get("locationId"))
                    .where(featureRoot.get("type").in(Arrays.asList(features)));

            predicates.add(locationRoot.get("id").in(featureSub));
        }

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

    public Optional<Location> getById(UUID locationId) {
        return locationRepository.findById(locationId);
    }

    public Location add(Location location) {

        return locationRepository.save(location);
    }

    public Long getLocationsCount() {
        return locationRepository.count();
    }


}
