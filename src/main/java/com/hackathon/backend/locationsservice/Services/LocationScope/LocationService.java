package com.hackathon.backend.locationsservice.Services.LocationScope;

import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.LocationScope.LocationCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.LocationScope.LocationPendingCopyCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.LocationScope.LocationPendingCopyReadDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.LocationScope.LocationReadDTO;
import com.hackathon.backend.locationsservice.DTOs.Mappers.LocationScope.LocationMapper;
import com.hackathon.backend.locationsservice.DTOs.Mappers.LocationScope.LocationPendingCopyMapper;
import com.hackathon.backend.locationsservice.DTOs.RecordDTOs.BarrierlessCriteriaScope.BarrierlessCriteriaCheckDTO;
import com.hackathon.backend.locationsservice.DTOs.RecordDTOs.BarrierlessCriteriaScope.BarrierlessCriteriaDTO;
import com.hackathon.backend.locationsservice.DTOs.RecordDTOs.BarrierlessCriteriaScope.BarrierlessCriteriaGroupDTO;
import com.hackathon.backend.locationsservice.DTOs.RecordDTOs.BarrierlessCriteriaScope.BarrierlessCriteriaTypeDTO;
import com.hackathon.backend.locationsservice.DTOs.RecordDTOs.LocationScope.LocationTypeWithGroupDTO;
import com.hackathon.backend.locationsservice.DTOs.SimilarLocationDTO;
import com.hackathon.backend.locationsservice.DTOs.ViewLists.LocationListViewDTO;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteria;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaCheck;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaGroup;
import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaType;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.Location;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.LocationType;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.additional.LocationPendingCopy;
import com.hackathon.backend.locationsservice.Domain.Enums.LocationStatusEnum;
import com.hackathon.backend.locationsservice.Domain.JSONB_POJOs.Pagination;
import com.hackathon.backend.locationsservice.Domain.JSONB_POJOs.WorkingHours;
import com.hackathon.backend.locationsservice.Domain.Verification;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaCheckRepository;
import com.hackathon.backend.locationsservice.Repositories.LocationScope.LocationPendingCopyRepository;
import com.hackathon.backend.locationsservice.Repositories.LocationScope.LocationRepository;
import com.hackathon.backend.locationsservice.Repositories.LocationScope.LocationScoreChgRepository;
import com.hackathon.backend.locationsservice.Repositories.LocationScope.LocationTypeRepository;
import com.hackathon.backend.locationsservice.Result.EntityErrors.EntityError;
import com.hackathon.backend.locationsservice.Result.EntityErrors.LocationError;
import com.hackathon.backend.locationsservice.Result.Result;
import com.hackathon.backend.locationsservice.Security.DTO.Domain.UserDTO;
import com.hackathon.backend.locationsservice.Security.Domain.User;
import com.hackathon.backend.locationsservice.Security.Services.UserService;
import com.hackathon.backend.locationsservice.Security.Services.UserServiceImpl;
import com.hackathon.backend.locationsservice.Services.GeneralService;
import com.hackathon.backend.locationsservice.Services.util.StringSimilarity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LocationService extends GeneralService<LocationMapper, LocationReadDTO, LocationCreateDTO, Location, LocationRepository> {

    private final LocationTypeRepository locationTypeRepository;
    private final BarrierlessCriteriaCheckRepository barrierlessCriteriaCheckRepository;
    private final LocationPendingCopyMapper locationPendingCopyMapper;
    private final LocationPendingCopyRepository locationPendingCopyRepository;
    private final LocationScoreChgRepository locationScoreChgRepository;
    private final UserServiceImpl userService;

    LocationService(LocationRepository locationRepository, LocationMapper locationMapper, LocationTypeRepository locationTypeRepository, BarrierlessCriteriaCheckRepository barrierlessCriteriaCheckRepository,
                    LocationPendingCopyMapper locationPendingCopyMapper, LocationPendingCopyRepository locationPendingCopyRepository,
                    LocationScoreChgRepository locationScoreChgRepository, UserServiceImpl userService) {

        super(locationRepository, Location.class, locationMapper);
        this.locationTypeRepository = locationTypeRepository;
        this.barrierlessCriteriaCheckRepository = barrierlessCriteriaCheckRepository;
        this.locationPendingCopyMapper = locationPendingCopyMapper;
        this.locationPendingCopyRepository = locationPendingCopyRepository;
        this.locationScoreChgRepository = locationScoreChgRepository;
        this.userService = userService;
    }

    @PersistenceContext
    private EntityManager entityManager;


    public Result<Location, LocationListViewDTO> getAll(Map<String, Object> params) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Location> cq = cb.createQuery(Location.class);
        Root<Location> locationRoot = cq.from(Location.class);

        List<Predicate> predicates = new ArrayList<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        boolean isAdmin = false;
        boolean isAuthenticated = false;

        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
            isAdmin = userDetails.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ADMIN"));
            isAuthenticated = true;
        }

        if (!isAdmin) {
            Predicate published = cb.equal(locationRoot.get("status"), LocationStatusEnum.published);

            if (isAuthenticated && username != null) {
                UserDTO user = userService.loadWholeUserByUsername(username);
                Predicate ownPending = cb.and(
                        cb.equal(locationRoot.get("status"), LocationStatusEnum.pending),
                        cb.equal(locationRoot.get("createdBy"), user.id())
                );
                predicates.add(cb.or(published, ownPending));
            } else {
                // Неавторизований — тільки опубліковані
                predicates.add(published);
            }
        }

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
            return Result.failure(EntityError.notFound(BarrierlessCriteriaGroup.class, locationCreateDTO.getType()));
        }
        Location newLocation = mapper.toEntity(locationCreateDTO);
        if (newLocation == null) {
            return Result.failure(EntityError.nullReference(type));
        }
        List<Location> locations = repository.findAll();
        if (checkNameDuplicates(locations, newLocation.getName())) {
            return Result.failure(EntityError.sameName(type, newLocation.getName()));
        }
        ;

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

        WorkingHours wh = newLocation.getWorkingHours();
        if (wh != null) {
            boolean invalidHours = Arrays.stream(WorkingHours.class.getDeclaredFields())
                    .filter(f -> f.getType().equals(WorkingHours.DayHours.class))
                    .map(f -> {
                        try {
                            f.setAccessible(true);
                            return (WorkingHours.DayHours) f.get(wh);
                        } catch (IllegalAccessException e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .anyMatch(day -> {
                        boolean hasOpen = day.getOpen() != null && !day.getOpen().isBlank();
                        boolean hasClose = day.getClose() != null && !day.getClose().isBlank();
                        return hasOpen ^ hasClose;
                    });

            if (invalidHours) {
                return Result.failure(LocationError.invalidWorkingHours());
            }
        }


        Location savedLocation = repository.save(newLocation);
        Result<Location, LocationReadDTO> res = Result.success();
        res.entity = savedLocation;
        res.entityDTO = mapper.toDto(savedLocation);

        return res;

    }

    public Long getLocationsCount() {
        return repository.count();
    }


    @Transactional
    public Result<Location, LocationReadDTO> mergeBarrierLocationChecks(UUID newLocationId, UUID oldLocationId) {
        Optional<Location> newLocationOptional = repository.findById(newLocationId);
        Optional<Location> oldLocationOptional = repository.findById(oldLocationId);

        if (newLocationOptional.isEmpty()) {
            return Result.failure(EntityError.notFound(type, newLocationId));
        }
        if (oldLocationOptional.isEmpty()) {
            return Result.failure(EntityError.notFound(type, oldLocationId));
        }

        Location oldLocation = oldLocationOptional.get();
        Location newLocation = newLocationOptional.get();

        Set<BarrierlessCriteriaCheck> checksToMove = new HashSet<>(newLocation.getBarrierlessCriteriaChecks());

        for (BarrierlessCriteriaCheck check : checksToMove) {
            check.reassignTo(oldLocation, entityManager);
        }

        oldLocation.getBarrierlessCriteriaChecks().addAll(checksToMove);
        newLocation.getBarrierlessCriteriaChecks().clear();

        Location savedLocation = repository.save(oldLocation);
        repository.save(newLocation);

        Result<Location, LocationReadDTO> res = Result.success();
        res.entity = savedLocation;
        res.entityDTO = mapper.toDto(savedLocation);
        return res;
    }

    public Result<Location, LocationReadDTO> update(UUID locationId, Long locationPendingCopyId) {
        Optional<Location> locationOptional = repository.findById(locationId);
        if (locationOptional.isEmpty()) {
            return Result.failure(EntityError.notFound(Location.class, locationId));
        }
        Optional<LocationPendingCopy> locationPendingCopyOptional = locationPendingCopyRepository.findById(locationPendingCopyId);
        if (locationPendingCopyOptional.isEmpty()) {
            return Result.failure(EntityError.notFound(LocationPendingCopy.class, locationPendingCopyId));
        }

        Location oldLocation = locationOptional.get();
        LocationPendingCopy locationPendingCopy = locationPendingCopyOptional.get();
        List<Location> locations = repository.findAll();
        if (checkNameDuplicates(locations, locationPendingCopy.getName())) {
            return Result.failure(EntityError.sameName(type, locationPendingCopy.getName()));
        }

        oldLocation.setAddress(locationPendingCopy.getAddress());
        oldLocation.setName(locationPendingCopy.getName());
        oldLocation.setUpdatedAt(locationPendingCopy.getUpdatedAt());
        oldLocation.setDescription(locationPendingCopy.getDescription());
        oldLocation.setContacts(locationPendingCopy.getContacts());
        oldLocation.setStatus(LocationStatusEnum.published);

        oldLocation.setOrganizationId(locationPendingCopy.getOrganizationId());
        oldLocation.setWorkingHours(locationPendingCopy.getWorkingHours());

        Location savedLocation = repository.save(oldLocation);
        locationPendingCopyRepository.delete(locationPendingCopy);
        Result<Location, LocationReadDTO> res = Result.success();
        res.entity = savedLocation;
        res.entityDTO = mapper.toDto(savedLocation);

        return res;

    }

    public Result<LocationPendingCopy, LocationPendingCopyReadDTO> createPendingCopy(UUID locationId, LocationPendingCopyCreateDTO locationPendingCopyCreateDTO) {
        Optional<Location> locationOptional = repository.findById(locationId);
        if (locationOptional.isEmpty()) {
            return Result.failure(EntityError.notFound(Location.class, locationId));
        }
        locationPendingCopyCreateDTO.setLocationId(locationId);
        LocationPendingCopy locationPendingCopy = locationPendingCopyMapper.toEntity(locationPendingCopyCreateDTO);
        if (locationPendingCopy == null) {
            return Result.failure(EntityError.nullReference(type));
        }
        List<Location> locations = repository.findAll();
        if (checkNameDuplicates(locations, locationPendingCopy.getName())) {
            return Result.failure(EntityError.sameName(LocationPendingCopy.class, locationPendingCopy.getName()));
        }

        locationPendingCopy.setLocation(locationOptional.get());
        locationPendingCopy.setName(locationPendingCopyCreateDTO.getName());
        locationPendingCopy.setDescription(locationPendingCopyCreateDTO.getDescription());
        locationPendingCopy.setContacts(locationPendingCopyCreateDTO.getContacts());
        locationPendingCopy.setWorkingHours(locationPendingCopyCreateDTO.getWorkingHours());
        locationPendingCopy.setOrganizationId(locationPendingCopyCreateDTO.getOrganizationId());
        locationPendingCopy.setUpdatedAt(locationPendingCopyCreateDTO.getUpdatedAt());
        locationPendingCopy.setAddress(locationPendingCopyCreateDTO.getAddress());
        locationPendingCopy.setStatus(locationPendingCopyCreateDTO.getStatus());


        LocationPendingCopy savedLocationPendingCopy = locationPendingCopyRepository.save(locationPendingCopy);
        Result<LocationPendingCopy, LocationPendingCopyReadDTO> res = Result.success();
        res.entity = savedLocationPendingCopy;
        res.entityDTO = locationPendingCopyMapper.toDto(savedLocationPendingCopy);
        return res;
    }

    public Result<Location, LocationReadDTO> update(UUID locationId, LocationCreateDTO locationCreateDTO) {
        Optional<Location> locationOptional = repository.findById(locationId);
        if (locationOptional.isEmpty()) {
            return Result.failure(EntityError.notFound(Location.class, locationId));
        }
        Optional<LocationType> newLocationTypeOptional = locationTypeRepository.findById(locationCreateDTO.getType());
        if (newLocationTypeOptional.isEmpty()) {
            return Result.failure(EntityError.notFound(LocationType.class, locationCreateDTO.getType()));
        }
        Location newLocation = mapper.toEntity(locationCreateDTO);
        if (newLocation == null) {
            return Result.failure(EntityError.nullReference(type));
        }
        List<Location> locations = repository.findAll();
        if (checkNameDuplicates(locations, newLocation.getName())) {
            return Result.failure(EntityError.sameName(type, newLocation.getName()));
        }

        Location oldLocation = locationOptional.get();
        for (Location location_iter : locations) {
            if (!location_iter.getId().equals(newLocation.getId()) && location_iter.getCoordinates().equals(newLocation.getCoordinates())) {
                return Result.failure(LocationError.sameCoordinates(newLocation.getCoordinates()));
            }
        }
        oldLocation.setName(newLocation.getName());
        oldLocation.setCoordinates(newLocation.getCoordinates());
        oldLocation.setAddress(newLocation.getAddress());
        oldLocation.setCreatedAt(newLocation.getCreatedAt());
        oldLocation.setCreatedBy(newLocation.getCreatedBy());
        oldLocation.setUpdatedAt(newLocation.getUpdatedAt());
        oldLocation.setDescription(newLocation.getDescription());
        oldLocation.setContacts(newLocation.getContacts());
        oldLocation.setStatus(newLocation.getStatus());
        if (!newLocationTypeOptional.get().equals(oldLocation.getType())) {
            BarrierlessCriteriaGroup oldLocBarrierlessCriteriaGroup = oldLocation.getType().getBarrierlessCriteriaGroup();
            Set<BarrierlessCriteriaType> oldLocbarrierlessCriteriaTypes = oldLocBarrierlessCriteriaGroup.getBarrierlessCriteriaTypes();
            Set<BarrierlessCriteria> restrictedBarrierlessCriterias = new HashSet<>();
            for (BarrierlessCriteriaType criteriaType : oldLocbarrierlessCriteriaTypes) {
                restrictedBarrierlessCriterias.addAll(criteriaType.getBarrierlessCriterias());
            }
            Set<BarrierlessCriteriaCheck> locationBarrierlessCriteriaChecks = oldLocation.getBarrierlessCriteriaChecks();
            List<BarrierlessCriteriaCheck> toRemove = new ArrayList<>();
            for (BarrierlessCriteriaCheck locCheck : locationBarrierlessCriteriaChecks) {
                for (BarrierlessCriteria restrictCriteria : restrictedBarrierlessCriterias) {
                    if (locCheck.getBarrierlessCriteria().equals(restrictCriteria)) {
                        toRemove.add(locCheck);
                        break;
                    }
                }
            }
            toRemove.forEach(oldLocation.getBarrierlessCriteriaChecks()::remove);
            barrierlessCriteriaCheckRepository.deleteAll(toRemove);

        }
        oldLocation.setType(newLocationTypeOptional.get());
        oldLocation.setOrganizationId(newLocation.getOrganizationId());
        oldLocation.setLastVerifiedAt(newLocation.getLastVerifiedAt());
        oldLocation.setCoordinates(newLocation.getCoordinates());
        oldLocation.setWorkingHours(newLocation.getWorkingHours());
        oldLocation.setRejectionReason(newLocation.getRejectionReason());
        oldLocation.setOverallAccessibilityScore(newLocation.getOverallAccessibilityScore());

        Location savedLocation = repository.save(oldLocation);
        Result<Location, LocationReadDTO> res = Result.success();
        res.entity = savedLocation;
        res.entityDTO = mapper.toDto(savedLocation);

        return res;

    }

    //@Async
    @Scheduled(fixedDelay = 30 * 60 * 1000, initialDelay = 2 * 60 * 1000)
    @Transactional
    public void calculateBarrierlessScore() {

        List<UUID> uis = locationScoreChgRepository.getLocationScoreChg_locationId();

        if (uis.isEmpty()) {
            return;
        }

        Location location;

        for (UUID ui : uis) {

            location = repository.findById(ui).get();
            var types = location.getType().getBarrierlessCriteriaGroup().getBarrierlessCriteriaTypes();

            long total = 0;
            long barierlessCriteriaAllCount = 0;

            for (BarrierlessCriteriaType type : types) {

                Location finalLocation = location;

                long barrierlessCriteriaTypeCount = type.getBarrierlessCriterias().stream()
                        .map(barrierlessCriteria -> {
                            int score = barrierlessCriteriaCheckRepository
                                    .findAllByBarrierlessCriteria_IdAndLocation_Id(
                                            barrierlessCriteria.getId(),
                                            finalLocation.getId()
                                    )
                                    .stream()
                                    .mapToInt(check -> check.isHasIssue() ? -1 : 1)
                                    .sum();

                            return score >= 0;

                        })
                        .filter(isBarrierless -> isBarrierless)
                        .count();

                total += type.getBarrierlessCriterias().size();
                barierlessCriteriaAllCount += barrierlessCriteriaTypeCount;

            }

            int newScore = (int) Math.round(((double) barierlessCriteriaAllCount / total) * 100);
            location.setOverallAccessibilityScore(newScore);
            repository.save(location);
        }

    }

    public List<SimilarLocationDTO> findSimilar(LocationCreateDTO newLocDTO) {
        Location newLoc = mapper.toEntity(newLocDTO);
        List<Location> nearby = repository.findNearby(
                newLoc.getCoordinates().getY(),
                newLoc.getCoordinates().getX()
        );

        return nearby.stream()
                .map(existing -> {
                    double nameSim = StringSimilarity.likeness(existing.getName(), newLoc.getName());
                    double addrSim = StringSimilarity.likeness(existing.getAddress(), newLoc.getAddress());
                    double likeness = (nameSim + addrSim) / 2.0 * 100;

                    return new SimilarLocationDTO(mapper.toDto(existing), likeness);
                })
                .filter(dto -> dto.getLikeness() >= 80)
                .sorted((a, b) -> {
                    int typeCompare = Boolean.compare(
                            b.getLocation().getType().equals(newLoc.getType()),
                            a.getLocation().getType().equals(newLoc.getType())
                    );
                    if (typeCompare != 0) return typeCompare;
                    return Double.compare(b.getLikeness(), a.getLikeness());
                })
                .toList();
    }

    public Result<LocationType, LocationTypeWithGroupDTO> getCriteriaTree(UUID locationId, UUID userId) {
        Optional<Location> locationOptional = repository.findById(locationId);
        if (locationOptional.isEmpty()) {
            return Result.failure(EntityError.notFound(Location.class, locationId));
        }

        Location location = locationOptional.get();
        LocationType locationType = location.getType();
        BarrierlessCriteriaGroup group = locationType.getBarrierlessCriteriaGroup();

        List<BarrierlessCriteriaTypeDTO> typeDTOs = group.getBarrierlessCriteriaTypes().stream()
                .map(t -> new BarrierlessCriteriaTypeDTO(
                        t.getId(),
                        t.getName(),
                        t.getDescription(),
                        t.getBarrierlessCriterias().stream()
                                .map(c -> new BarrierlessCriteriaDTO(
                                        c.getId(),
                                        c.getName(),
                                        c.getDescription(),
                                        c.getBarrierlessCriteriaRank().name(),

                                        // 🔥 Фільтрація чеків по locationId і userId
                                        c.getBarrierlessCriteriaChecks().stream()
                                                .filter(ch ->
                                                        ch.getLocation() != null &&
                                                                ch.getLocation().getId().equals(locationId) &&
                                                                (userId == null || (ch.getUser() != null && ch.getUser().getId().equals(userId)))
                                                )
                                                .map(ch -> new BarrierlessCriteriaCheckDTO(
                                                        ch.getLocation().getId(),
                                                        ch.getBarrierlessCriteria().getId(),
                                                        ch.getUser().getId(),
                                                        ch.getComment(),
                                                        ch.isHasIssue()
                                                ))
                                                .toList()
                                ))
                                .toList()
                ))
                .toList();

        LocationTypeWithGroupDTO locationTypeWithGroupDTO = new LocationTypeWithGroupDTO(
                locationType.getId(),
                locationType.getName(),
                locationType.getDescription(),
                new BarrierlessCriteriaGroupDTO(
                        group.getId(),
                        group.getName(),
                        group.getDescription(),
                        typeDTOs
                )
        );

        Result<LocationType, LocationTypeWithGroupDTO> res = Result.success();
        res.entity = locationType;
        res.entityDTO = locationTypeWithGroupDTO;
        return res;
    }


}
