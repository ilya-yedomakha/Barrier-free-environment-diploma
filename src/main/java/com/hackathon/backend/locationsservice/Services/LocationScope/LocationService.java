package com.hackathon.backend.locationsservice.Services.LocationScope;

import com.hackathon.backend.locationsservice.AMPQElements.LocationCreationEventPub;
import com.hackathon.backend.locationsservice.AMPQElements.ModerationTextEventPub;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.LocationScope.LocationCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Create.LocationScope.LocationPendingCopyCreateDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.LocationScope.LocationPendingCopyReadDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.LocationScope.LocationReadDTO;
import com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.LocationScope.LocationTypeReadDTO;
import com.hackathon.backend.locationsservice.DTOs.Mappers.LocationScope.LocationMapper;
import com.hackathon.backend.locationsservice.DTOs.Mappers.LocationScope.LocationPendingCopyMapper;
import com.hackathon.backend.locationsservice.DTOs.RabbitMQDTOs.text_moderation.ModerationElementType;
import com.hackathon.backend.locationsservice.DTOs.Mappers.LocationScope.LocationTypeMapper;
import com.hackathon.backend.locationsservice.DTOs.RecordDTOs.BarrierlessCriteriaScope.BarrierlessCriteriaCheckDTO;
import com.hackathon.backend.locationsservice.DTOs.RecordDTOs.BarrierlessCriteriaScope.BarrierlessCriteriaDTO;
import com.hackathon.backend.locationsservice.DTOs.RecordDTOs.BarrierlessCriteriaScope.BarrierlessCriteriaGroupDTO;
import com.hackathon.backend.locationsservice.DTOs.RecordDTOs.BarrierlessCriteriaScope.BarrierlessCriteriaTypeDTO;
import com.hackathon.backend.locationsservice.DTOs.RecordDTOs.LocationScope.LocationTypeWithGroupDTO;
import com.hackathon.backend.locationsservice.DTOs.RecordDTOs.LocationScope.RejectionReason;
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
import com.hackathon.backend.locationsservice.Result.EntityErrors.UserError;
import com.hackathon.backend.locationsservice.Result.Result;
import com.hackathon.backend.locationsservice.Security.DTO.Domain.UserDTO;
import com.hackathon.backend.locationsservice.Security.Domain.User;
import com.hackathon.backend.locationsservice.Security.Services.UserService;
import com.hackathon.backend.locationsservice.Security.Services.UserServiceImpl;
import com.hackathon.backend.locationsservice.Services.GeneralService;
import com.hackathon.backend.locationsservice.Services.util.StringSimilarity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
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

import java.time.LocalDateTime;
import java.util.*;

@Service
public class LocationService extends GeneralService<LocationMapper, LocationReadDTO, LocationCreateDTO, Location, LocationRepository> {

    private final LocationTypeRepository locationTypeRepository;
    private final BarrierlessCriteriaCheckRepository barrierlessCriteriaCheckRepository;
    private final LocationPendingCopyMapper locationPendingCopyMapper;
    private final LocationTypeMapper locationTypeMapper;
    private final LocationPendingCopyRepository locationPendingCopyRepository;
    private final LocationScoreChgRepository locationScoreChgRepository;
    private final UserServiceImpl userService;
    private final ModerationTextEventPub moderationTextEventPub;

    LocationService(LocationRepository locationRepository, LocationMapper locationMapper,
                    LocationTypeRepository locationTypeRepository, BarrierlessCriteriaCheckRepository barrierlessCriteriaCheckRepository,
                    LocationPendingCopyMapper locationPendingCopyMapper, LocationTypeMapper locationTypeMapper,
                    LocationPendingCopyRepository locationPendingCopyRepository,
                    LocationScoreChgRepository locationScoreChgRepository, UserServiceImpl userService,
                    ModerationTextEventPub moderationTextEventPub) {

        super(locationRepository, Location.class, locationMapper);
        this.locationTypeRepository = locationTypeRepository;
        this.barrierlessCriteriaCheckRepository = barrierlessCriteriaCheckRepository;
        this.locationPendingCopyMapper = locationPendingCopyMapper;
        this.locationTypeMapper = locationTypeMapper;
        this.locationPendingCopyRepository = locationPendingCopyRepository;
        this.locationScoreChgRepository = locationScoreChgRepository;
        this.userService = userService;
        this.moderationTextEventPub = moderationTextEventPub;
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
            String queryText = ((String) params.get("query")).toLowerCase();
            String pattern = "%" + queryText + "%";
            predicates.add(
                    cb.or(
                            cb.like(cb.lower(locationRoot.get("name")), pattern),
                            cb.like(cb.lower(locationRoot.get("address")), pattern)
                    )
            );
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
            point.setSRID(5564);

            Expression<Double> distanceExpr = cb.function("ST_Distance", Double.class,
                    locationRoot.get("coordinates"), cb.literal(point));

            predicates.add(cb.lessThanOrEqualTo(distanceExpr, radius));
        }

        cq.select(locationRoot).where(cb.and(predicates.toArray(new Predicate[0])));

        TypedQuery<Location> query = entityManager.createQuery(cq);

        long countLocations = getLocationsCount();

        int page = 1;
        Integer limit = null; // необмежений за замовчуванням

        if (params.get("limit") != null) {
            int paramLimit = ((Number) params.get("limit")).intValue();
            if (paramLimit > 0) {
                limit = paramLimit;
            }
        }

        if (params.get("page") != null) {
            int paramPage = ((Number) params.get("page")).intValue();
            page = paramPage > 0 ? paramPage : 1;
        }

        // Якщо ліміт заданий — застосовуємо пагінацію
        if (limit != null) {
            int firstResult = (page - 1) * limit;
            query.setFirstResult(firstResult);
            query.setMaxResults(limit);
        }

        Pagination pagination = new Pagination(
                page,
                limit != null ? limit : (int) countLocations,
                countLocations,
                limit != null && limit > 0 ? (countLocations / limit) : 1
        );

        List<Location> entities = query.getResultList();
        Result<Location, LocationListViewDTO> res = Result.success();
        res.setEntities(entities);
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

        if (isAuthenticated && username != null) {
            UserDTO user = userService.loadWholeUserByUsername(username);
            newLocation.setCreatedBy(user.id());
            if (isAdmin) {
                newLocation.setStatus(LocationStatusEnum.published);
            }
        }

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

        moderationTextEventPub.sendTextForModeration(savedLocation.getId().toString(), ModerationElementType.LOCATION,
                "Назва: ".concat(savedLocation.getName()).concat(", опис: ").concat(savedLocation.getDescription()));

        return res;

    }

    public Result<Location, LocationReadDTO> isValid(LocationCreateDTO locationCreateDTO) {
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

        for (Location location : locations) {
            if (location.getCoordinates().equals(newLocation.getCoordinates())) {
                return Result.failure(LocationError.sameCoordinates(newLocation.getCoordinates()));
            }
        }

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


        Result<Location, LocationReadDTO> res = Result.success();
        res.entity = newLocation;
        res.entityDTO = mapper.toDto(newLocation);

        return res;

    }

    public Long getLocationsCount() {
        return repository.count();
    }

    @Deprecated
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

        moderationTextEventPub.sendTextForModeration(savedLocation.getId().toString(), ModerationElementType.LOCATION,
                "Назва: ".concat(newLocation.getName()).concat(", опис: ").concat(newLocation.getDescription()));

        return res;
    }

    public Result<Location, LocationReadDTO> update(UUID locationId, Long locationPendingCopyId, LocationPendingCopyCreateDTO locationPendingCopyCreateDTO) {
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
        if (!oldLocation.getId().equals(locationPendingCopy.getLocation().getId())) {
            return Result.failure(LocationError.locationMismatch(locationId, locationPendingCopy.getLocation().getId()));
        }
        if (oldLocation.getStatus().equals(LocationStatusEnum.published)) {
            return Result.failure(LocationError.locationPublishedUpdateFromDuplicateImpossible(locationId));
        }
        List<Location> locations = repository.findAll();
        locations.remove(oldLocation);
        if (checkNameDuplicates(locations, locationPendingCopyCreateDTO.getName())) {
            return Result.failure(EntityError.sameName(type, locationPendingCopyCreateDTO.getName()));
        }

        oldLocation.setAddress(locationPendingCopyCreateDTO.getAddress());
        oldLocation.setName(locationPendingCopyCreateDTO.getName());
        oldLocation.setUpdatedAt(locationPendingCopy.getUpdatedAt());
        oldLocation.setUpdatedBy(locationPendingCopy.getUpdatedBy());
        oldLocation.setDescription(locationPendingCopyCreateDTO.getDescription());
        oldLocation.setContacts(locationPendingCopyCreateDTO.getContacts());
        oldLocation.setStatus(LocationStatusEnum.published);

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

        if (isAuthenticated && username != null) {
            UserDTO user = userService.loadWholeUserByUsername(username);
            if (isAdmin) {
                oldLocation.setLastVerifiedAt(LocalDateTime.now());
                oldLocation.setLastVerifiedBy(user.id());
            }
        }


        oldLocation.setOrganizationId(locationPendingCopyCreateDTO.getOrganizationId());
        oldLocation.setWorkingHours(locationPendingCopyCreateDTO.getWorkingHours());

        Location savedLocation = repository.save(oldLocation);
        locationPendingCopyRepository.delete(locationPendingCopy);
        Result<Location, LocationReadDTO> res = Result.success();
        res.entity = savedLocation;
        res.entityDTO = mapper.toDto(savedLocation);

        return res;

    }

    public Result<Location, LocationReadDTO> updateByDuplicate(UUID locationId, UUID duplicateId, LocationPendingCopyCreateDTO locationPendingCopyCreateDTO) {
        Optional<Location> locationOptional = repository.findById(locationId);
        if (locationOptional.isEmpty()) {
            return Result.failure(EntityError.notFound(Location.class, locationId));
        }
//        Optional<LocationPendingCopy> locationPendingCopyOptional = locationPendingCopyRepository.findById(locationPendingCopyId);
//        if (locationPendingCopyOptional.isEmpty()) {
//            return Result.failure(EntityError.notFound(LocationPendingCopy.class, locationPendingCopyId));
//        }
        Optional<Location> duplicateOptional = repository.findById(duplicateId);
        if (duplicateOptional.isEmpty()) {
            return Result.failure(EntityError.notFound(Location.class, duplicateId));
        }

        Location newLocation = locationOptional.get();
        Location duplLocation = duplicateOptional.get();
//        if (!oldLocation.getId().equals(locationPendingCopy.getLocation().getId())) {
//            return Result.failure(LocationError.locationMismatch(locationId, locationPendingCopy.getLocation().getId()));
//        }
        List<Location> locations = repository.findAll();
        locations.removeAll(Arrays.asList(duplLocation, newLocation));
        if (checkNameDuplicates(locations, locationPendingCopyCreateDTO.getName())) {
            return Result.failure(EntityError.sameName(type, locationPendingCopyCreateDTO.getName()));
        }

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

        if (isAuthenticated && username != null) {
            UserDTO user = userService.loadWholeUserByUsername(username);
            if (isAdmin) {
                duplLocation.setLastVerifiedAt(LocalDateTime.now());
                duplLocation.setLastVerifiedBy(user.id());
            }
        }

        duplLocation.setAddress(locationPendingCopyCreateDTO.getAddress());
        duplLocation.setName(locationPendingCopyCreateDTO.getName());
        duplLocation.setUpdatedAt(locationPendingCopyCreateDTO.getUpdatedAt());
        duplLocation.setUpdatedBy(locationPendingCopyCreateDTO.getUpdatedBy());
        duplLocation.setDescription(locationPendingCopyCreateDTO.getDescription());
        duplLocation.setContacts(locationPendingCopyCreateDTO.getContacts());
        duplLocation.setStatus(LocationStatusEnum.published);

        duplLocation.setOrganizationId(locationPendingCopyCreateDTO.getOrganizationId());
        duplLocation.setWorkingHours(locationPendingCopyCreateDTO.getWorkingHours());

        Location savedLocation = repository.save(duplLocation);
        repository.delete(newLocation);
        Result<Location, LocationReadDTO> res = Result.success();
        res.entity = savedLocation;
        res.entityDTO = mapper.toDto(savedLocation);

//        moderationTextEventPub.sendTextForModeration(newLocation.getId().toString(), ModerationElementType.LOCATION,
//                newLocation.getName().concat(" ").concat(newLocation.getDescription()));

        return res;

    }

    public Result<LocationPendingCopy, LocationPendingCopyReadDTO> createPendingCopy(UUID locationId, LocationPendingCopyCreateDTO locationPendingCopyCreateDTO) {
        Optional<Location> locationOptional = repository.findById(locationId);
        if (locationOptional.isEmpty()) {
            return Result.failure(EntityError.notFound(Location.class, locationId));
        }
        if (locationOptional.get().getStatus() != LocationStatusEnum.published) {
            return Result.failure(LocationError.notPublished(locationId));
        }
        locationPendingCopyCreateDTO.setLocationId(locationId);
        LocationPendingCopy locationPendingCopy = locationPendingCopyMapper.toEntity(locationPendingCopyCreateDTO);
        if (locationPendingCopy == null) {
            return Result.failure(EntityError.nullReference(type));
        }

        List<Location> locations = repository.findAll();
        Location location = locationOptional.get();
        locations.remove(location);
        if (checkNameDuplicates(locations, locationPendingCopy.getName())) {
            return Result.failure(EntityError.sameName(LocationPendingCopy.class, locationPendingCopy.getName()));
        }

        locationPendingCopy.setLocation(locationOptional.get());
        locationPendingCopy.setName(locationPendingCopyCreateDTO.getName());
        locationPendingCopy.setDescription(locationPendingCopyCreateDTO.getDescription());
        locationPendingCopy.setContacts(locationPendingCopyCreateDTO.getContacts());
        locationPendingCopy.setWorkingHours(locationPendingCopyCreateDTO.getWorkingHours());
        locationPendingCopy.setOrganizationId(locationPendingCopyCreateDTO.getOrganizationId());
        locationPendingCopy.setUpdatedAt(LocalDateTime.now());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        UUID userId = null;
        boolean isAuthenticated = false;

        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
            isAuthenticated = true;
        }
        if (isAuthenticated && username != null) {
            UserDTO user = userService.loadWholeUserByUsername(username);
            userId = user.id();
        }

        final UUID currentUserId = userId;

        List<LocationPendingCopy> previousUpdates = locationPendingCopyRepository.findAllByLocation_IdAndUpdatedBy(locationId, currentUserId);
        if (previousUpdates != null && !previousUpdates.isEmpty()) {
            locationPendingCopyRepository.deleteAll(previousUpdates);
        }

        locationPendingCopy.setUpdatedBy(currentUserId);
        locationPendingCopy.setAddress(locationPendingCopyCreateDTO.getAddress());
        locationPendingCopy.setStatus(locationPendingCopyCreateDTO.getStatus());


        LocationPendingCopy savedLocationPendingCopy = locationPendingCopyRepository.save(locationPendingCopy);
        Result<LocationPendingCopy, LocationPendingCopyReadDTO> res = Result.success();
        res.entity = savedLocationPendingCopy;
        res.entityDTO = locationPendingCopyMapper.toDto(savedLocationPendingCopy);
        return res;
    }

    public Result<Location, LocationReadDTO> getUserModifiedLocations(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        UUID userId = null;
        boolean isAuthenticated = false;

        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
            isAuthenticated = true;
        }
        if (isAuthenticated && username != null) {
            UserDTO user = userService.loadWholeUserByUsername(username);
            userId = user.id();
        }

        final UUID currentUserId = userId;


        List<Location> locationsCreatedBy = repository.findAllByCreatedBy(currentUserId);
        List<Location> locationsUpdatedBy = repository.findAllByUpdatedBy(currentUserId);

        List<BarrierlessCriteriaCheck> barrierlessCriteriaChecksOfUser = barrierlessCriteriaCheckRepository.findAllByUser_Id(currentUserId);

        List<Location> locationsByChecks = new ArrayList<>();

        for (BarrierlessCriteriaCheck check : barrierlessCriteriaChecksOfUser){
            locationsByChecks.add(check.getLocation());
        }

        List<LocationPendingCopy> pendingLocationsOfUser = locationPendingCopyRepository.getLocationPendingCopiesByUpdatedBy(currentUserId);
        List<Location> locationsByPendings = new ArrayList<>();

        for (LocationPendingCopy locationPendingCopy : pendingLocationsOfUser){
            locationsByPendings.add(locationPendingCopy.getLocation());
        }

        HashSet<Location> combinedLocations = new HashSet<>();
        combinedLocations.addAll(locationsCreatedBy);
        combinedLocations.addAll(locationsUpdatedBy);
        combinedLocations.addAll(locationsByPendings);
        combinedLocations.addAll(locationsByChecks);


        Result<Location, LocationReadDTO> res = Result.success();
        res.entities = combinedLocations.stream().toList();
        res.entityDTOs = res.entities.stream().map(mapper::toDto).toList();
        return res;

    }

    public Result<Location, LocationReadDTO> getUserModifiedLocationsByUsername(String username){

        UserDTO user = userService.loadWholeUserByUsername(username);

        if (user == null){
            return Result.failure(UserError.notFound(username));
        }

        final UUID currentUserId = user.id();


        List<Location> locationsCreatedBy = repository.findAllByCreatedBy(currentUserId);
        List<Location> locationsUpdatedBy = repository.findAllByUpdatedBy(currentUserId);

        List<BarrierlessCriteriaCheck> barrierlessCriteriaChecksOfUser = barrierlessCriteriaCheckRepository.findAllByUser_Id(currentUserId);

        List<Location> locationsByChecks = new ArrayList<>();

        for (BarrierlessCriteriaCheck check : barrierlessCriteriaChecksOfUser){
            locationsByChecks.add(check.getLocation());
        }

        List<LocationPendingCopy> pendingLocationsOfUser = locationPendingCopyRepository.getLocationPendingCopiesByUpdatedBy(currentUserId);
        List<Location> locationsByPendings = new ArrayList<>();

        for (LocationPendingCopy locationPendingCopy : pendingLocationsOfUser){
            locationsByPendings.add(locationPendingCopy.getLocation());
        }

        HashSet<Location> combinedLocations = new HashSet<>();
        combinedLocations.addAll(locationsCreatedBy);
        combinedLocations.addAll(locationsUpdatedBy);
        combinedLocations.addAll(locationsByPendings);
        combinedLocations.addAll(locationsByChecks);


        Result<Location, LocationReadDTO> res = Result.success();
        res.entities = combinedLocations.stream().toList();
        res.entityDTOs = res.entities.stream().map(mapper::toDto).toList();
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
        locations.remove(locationOptional.get());
        if (checkNameDuplicates(locations, newLocation.getName())) {
            return Result.failure(EntityError.sameName(type, newLocation.getName()));
        }

        Location oldLocation = locationOptional.get();
        for (Location location_iter : locations) {
            if (!location_iter.getId().equals(newLocation.getId()) && location_iter.getCoordinates().equals(newLocation.getCoordinates())) {
                return Result.failure(LocationError.sameCoordinates(newLocation.getCoordinates()));
            }
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        UUID userId = null;
        boolean isAuthenticated = false;
        boolean isAdmin = false;

        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
            isAdmin = userDetails.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ADMIN"));
            isAuthenticated = true;
        }
        if (isAuthenticated && username != null) {
            UserDTO user = userService.loadWholeUserByUsername(username);
            userId = user.id();
            if (isAdmin) {
                oldLocation.setLastVerifiedAt(LocalDateTime.now());
                oldLocation.setLastVerifiedBy(userId);
            }
        }

        final UUID currentUserId = userId;
        oldLocation.setName(newLocation.getName());
        oldLocation.setCoordinates(newLocation.getCoordinates());
        oldLocation.setAddress(newLocation.getAddress());
//        oldLocation.setCreatedAt(newLocation.getCreatedAt());
//        oldLocation.setCreatedBy(newLocation.getCreatedBy());
        oldLocation.setUpdatedAt(LocalDateTime.now());
        oldLocation.setUpdatedBy(currentUserId);
        oldLocation.setDescription(newLocation.getDescription());
        oldLocation.setContacts(newLocation.getContacts());
        oldLocation.setStatus(LocationStatusEnum.published);
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
        oldLocation.setLastVerifiedAt(LocalDateTime.now());
        oldLocation.setLastVerifiedBy(currentUserId);
        oldLocation.setWorkingHours(newLocation.getWorkingHours());
        oldLocation.setRejectionReason(newLocation.getRejectionReason());
        oldLocation.setOverallAccessibilityScore(newLocation.getOverallAccessibilityScore());

        Location savedLocation = repository.save(oldLocation);
        Result<Location, LocationReadDTO> res = Result.success();
        res.entity = savedLocation;
        res.entityDTO = mapper.toDto(savedLocation);

        moderationTextEventPub.sendTextForModeration(savedLocation.getId().toString(), ModerationElementType.LOCATION,
                "Назва: ".concat(savedLocation.getName()).concat(", опис: ").concat(savedLocation.getDescription()));

        return res;

    }

    @Async
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

    public List<LocationReadDTO> findSimilar(LocationCreateDTO newLocDTO) {
        Location newLoc = mapper.toEntity(newLocDTO);

        List<Location> nearbySimilar = repository.findNearbySimilarLocations(
                newLoc.getCoordinates().getY(),  // lat
                newLoc.getCoordinates().getX(),  // lng
                newLoc.getName(),
                newLoc.getAddress()
        );

        return nearbySimilar.stream().map(mapper::toDto).toList();
    }

    public List<LocationReadDTO> findSimilarById(UUID id) {
        Location current = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + id));

        List<Location> nearbySimilar = repository.findNearbySimilarLocations(
                current.getCoordinates().getY(),  // lat
                current.getCoordinates().getX(),  // lng
                current.getName(),
                current.getAddress()
        );

        // 🔹 Відфільтровуємо саму локацію
        return nearbySimilar.stream()
                .filter(loc -> !Objects.equals(loc.getId(), id))
                .map(mapper::toDto)
                .toList();
    }


    public Result<LocationType, LocationTypeWithGroupDTO> getCriteriaTree(UUID locationId) {
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

                                        c.getBarrierlessCriteriaChecks().stream()
                                                .filter(ch -> ch.getLocation() != null && ch.getLocation().getId().equals(locationId))
                                                .map(ch -> new BarrierlessCriteriaCheckDTO(
                                                        ch.getLocation().getId(),
                                                        ch.getBarrierlessCriteria().getId(),
                                                        ch.getUser().getId(),
                                                        ch.getComment(),
                                                        ch.isHasIssue(),
                                                        ch.getImageServiceId()
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


    public Result<LocationType, LocationTypeWithGroupDTO> getCriteriaTreeByUser(UUID locationId) {
        Optional<Location> locationOptional = repository.findById(locationId);
        if (locationOptional.isEmpty()) {
            return Result.failure(EntityError.notFound(Location.class, locationId));
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        UUID userId = null;
        boolean isAuthenticated = false;

        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
            isAuthenticated = true;
        }
        if (isAuthenticated && username != null) {
            UserDTO user = userService.loadWholeUserByUsername(username);
            userId = user.id();
        }

        final UUID currentUserId = userId;

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
                                                                (currentUserId == null || (ch.getUser() != null && ch.getUser().getId().equals(currentUserId)))
                                                )
                                                .map(ch -> new BarrierlessCriteriaCheckDTO(
                                                        ch.getLocation().getId(),
                                                        ch.getBarrierlessCriteria().getId(),
                                                        ch.getUser().getId(),
                                                        ch.getComment(),
                                                        ch.isHasIssue(),
                                                        ch.getImageServiceId()
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


    public Result<LocationPendingCopy, LocationPendingCopyReadDTO> getUserPendingLocations() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        UUID userId = null;
        boolean isAuthenticated = false;

        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
            isAuthenticated = true;
        }
        if (isAuthenticated && username != null) {
            UserDTO user = userService.loadWholeUserByUsername(username);
            userId = user.id();
        }

        final UUID currentUserId = userId;

        List<LocationPendingCopy> entities = locationPendingCopyRepository.getLocationPendingCopiesByUpdatedBy(currentUserId);
        Result<LocationPendingCopy, LocationPendingCopyReadDTO> res = Result.success();
        res.setEntities(entities);
        res.setEntityDTOs(entities.stream().map(locationPendingCopyMapper::toDto).toList());
        return res;
    }

    public Result<LocationPendingCopy, LocationPendingCopyReadDTO> getAllPendingLocations() {
        List<LocationPendingCopy> entities = locationPendingCopyRepository.findAll();
        Result<LocationPendingCopy, LocationPendingCopyReadDTO> res = Result.success();
        res.setEntities(entities);
        res.entityDTOs = entities.stream().map(locationPendingCopyMapper::toDto).toList();
        return res;
    }

    public Result<LocationPendingCopy, LocationPendingCopyReadDTO> getPendingLocationsByLocationId(UUID locationId) {
        Optional<Location> locationOptional = repository.findById(locationId);
        if (locationOptional.isEmpty()) {
            return Result.failure(EntityError.notFound(Location.class, locationId));
        }

        List<LocationPendingCopy> entities = locationPendingCopyRepository.getLocationPendingCopiesByLocation(locationOptional.get());
        Result<LocationPendingCopy, LocationPendingCopyReadDTO> res = Result.success();
        res.setEntities(entities);
        res.entityDTOs = entities.stream().map(locationPendingCopyMapper::toDto).toList();
        return res;
    }

    public Result<LocationType, LocationTypeReadDTO> getLocationTypeByLocationId(UUID locationId) {
        Optional<Location> locationOptional = repository.findById(locationId);
        if (locationOptional.isEmpty()) {
            return Result.failure(EntityError.notFound(Location.class, locationId));
        }

        Result<LocationType, LocationTypeReadDTO> res = Result.success();
        res.setEntity(locationOptional.get().getType());
        res.setEntityDTO(locationTypeMapper.toDto(locationOptional.get().getType()));
        return res;
    }

    public Result<Location, LocationReadDTO> changeStatus(UUID locationId, String status, String rejectionReason) {
        Optional<Location> locationOptional = repository.findById(locationId);
        if (locationOptional.isEmpty()) {
            return Result.failure(EntityError.notFound(Location.class, locationId));
        }

        Location location = locationOptional.get();

        // ✅ Перевірка валідності статусу
        LocationStatusEnum newStatus;
        try {
            newStatus = LocationStatusEnum.valueOf(status.toLowerCase());
        } catch (IllegalArgumentException e) {
            return Result.failure(EntityError.invalid("Invalid status: " + status));
        }

        LocationStatusEnum currentStatus = location.getStatus();

        // ✅ Дозволені переходи:
        // pending → published | rejected
        // published → pending
        // rejected → pending
        boolean allowedTransition =
                (currentStatus == LocationStatusEnum.pending && (newStatus == LocationStatusEnum.published || newStatus == LocationStatusEnum.rejected)) ||
                        (currentStatus == LocationStatusEnum.published && newStatus == LocationStatusEnum.pending) ||
                        (currentStatus == LocationStatusEnum.rejected && newStatus == LocationStatusEnum.pending);

        if (!allowedTransition) {
            return Result.failure(EntityError.invalid(
                    String.format("Cannot change status from %s to %s", currentStatus, newStatus)
            ));
        }

        // ✅ Якщо відхилено — потрібна причина
        if (newStatus == LocationStatusEnum.rejected) {
            if (rejectionReason == null || rejectionReason.trim().isEmpty()) {
                return Result.failure(EntityError.invalid("Rejection reason is required for rejected status"));
            }
            location.setRejectionReason(rejectionReason.trim());
        } else {
            location.setRejectionReason(null);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        UUID userId = null;
        boolean isAuthenticated = false;
        boolean isAdmin = false;

        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
            isAdmin = userDetails.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ADMIN"));
            isAuthenticated = true;
        }
        if (isAuthenticated && username != null) {
            UserDTO user = userService.loadWholeUserByUsername(username);
            userId = user.id();
            if (isAdmin) {
                location.setLastVerifiedAt(LocalDateTime.now());
                location.setLastVerifiedBy(userId);
            }
        }

        // ✅ Оновлення статусу
        location.setStatus(newStatus);

        Location savedLocation = repository.save(location);

        Result<Location, LocationReadDTO> res = Result.success();
        res.entity = savedLocation;
        res.entityDTO = mapper.toDto(savedLocation);
        return res;
    }

    public Result<Location, LocationReadDTO> deleteLocation(UUID locationId) {
        Optional<Location> locationOptional = repository.findById(locationId);
        if (locationOptional.isEmpty()) {
            return Result.failure(EntityError.notFound(Location.class, locationId));
        }

        Location location = locationOptional.get();

        List<LocationPendingCopy> locationPendingCopies = locationPendingCopyRepository.getLocationPendingCopiesByLocation(location);

        locationPendingCopyRepository.deleteAll(locationPendingCopies);

        List<BarrierlessCriteriaCheck> barrierlessCriteriaChecks = barrierlessCriteriaCheckRepository.findAllByLocation_Id(locationId);

        barrierlessCriteriaCheckRepository.deleteAll(barrierlessCriteriaChecks);

        repository.delete(location);

        Result<Location, LocationReadDTO> res = Result.success();
        res.entity = location;
        res.entityDTO = mapper.toDto(location);
        return res;
    }

    public Result<LocationPendingCopy, LocationPendingCopyReadDTO> getPendingLocationOfUserByLocationId(UUID locationId) {
        Optional<Location> locationOptional = repository.findById(locationId);
        if (locationOptional.isEmpty()) {
            return Result.failure(EntityError.notFound(Location.class, locationId));
        }
        Location location = locationOptional.get();

        List<LocationPendingCopy> locationPendingCopies = locationPendingCopyRepository.getLocationPendingCopiesByLocation(location);

        LocationPendingCopy userLocationPendingCopy = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        UUID userId = null;
        boolean isAuthenticated = false;

        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
            isAuthenticated = true;
        }
        if (isAuthenticated && username != null) {
            UserDTO user = userService.loadWholeUserByUsername(username);
            userId = user.id();
        }

        final UUID currentUserId = userId;

        for (LocationPendingCopy copy : locationPendingCopies){
            if (copy.getUpdatedBy().equals(currentUserId)){
                userLocationPendingCopy = copy;
                break;
            }
        }
        Result<LocationPendingCopy, LocationPendingCopyReadDTO> res = Result.success();
        res.entity = userLocationPendingCopy;
        res.entityDTO = locationPendingCopyMapper.toDto(userLocationPendingCopy);
        return res;

    }

    public Result<LocationPendingCopy, LocationPendingCopyReadDTO> rejectPendingLocation(Long pendingId, RejectionReason rejectionReason) {
        Optional<LocationPendingCopy> locationOptional = locationPendingCopyRepository.findById(pendingId);
        if (locationOptional.isEmpty()) {
            return Result.failure(EntityError.notFound(LocationPendingCopy.class, pendingId));
        }

        LocationPendingCopy locationPendingCopy = locationOptional.get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        UUID userId = null;
        boolean isAuthenticated = false;

        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
            isAuthenticated = true;
        }
        if (isAuthenticated && username != null) {
            UserDTO user = userService.loadWholeUserByUsername(username);
            userId = user.id();
        }

        final UUID currentUserId = userId;
        locationPendingCopy.setRejectedBy(currentUserId);
        locationPendingCopy.setStatus(LocationStatusEnum.rejected);
        locationPendingCopy.setRejectedAt(LocalDateTime.now());
        locationPendingCopy.setRejectionReason(rejectionReason.rejectionReason());

        LocationPendingCopy savedLocationPendingCopy = locationPendingCopyRepository.save(locationPendingCopy);
        Result<LocationPendingCopy, LocationPendingCopyReadDTO> res = Result.success();
        res.entity = savedLocationPendingCopy;
        res.entityDTO = locationPendingCopyMapper.toDto(savedLocationPendingCopy);
        return res;
    }

    public Result<LocationPendingCopy, LocationPendingCopyReadDTO> getUserPendingLocationsByLocationId(UUID locationId) {
        Optional<Location> locationOptional = repository.findById(locationId);
        if (locationOptional.isEmpty()) {
            return Result.failure(EntityError.notFound(Location.class, locationId));
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        UUID userId = null;
        boolean isAuthenticated = false;

        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
            isAuthenticated = true;
        }
        if (isAuthenticated && username != null) {
            UserDTO user = userService.loadWholeUserByUsername(username);
            userId = user.id();
        }

        final UUID currentUserId = userId;

        Location location = locationOptional.get();
        List<LocationPendingCopy> copies = locationPendingCopyRepository.getLocationPendingCopiesByLocation(location);
        copies.removeIf(copy -> !copy.getUpdatedBy().equals(currentUserId));

        Result<LocationPendingCopy, LocationPendingCopyReadDTO> res = Result.success();
        res.entities = copies;
        res.entityDTOs = copies.stream().map(locationPendingCopyMapper::toDto).toList();
        return res;

    }

    public Result<LocationPendingCopy, LocationPendingCopyReadDTO> deletePending(Long pendingId) {
        Optional<LocationPendingCopy> locationPendingCopyOptional = locationPendingCopyRepository.findById(pendingId);
        if (locationPendingCopyOptional.isEmpty()) {
            return Result.failure(EntityError.notFound(LocationPendingCopy.class, pendingId));
        }

        LocationPendingCopy locationPendingCopy = locationPendingCopyOptional.get();

        Result<LocationPendingCopy, LocationPendingCopyReadDTO> res = Result.success();
        locationPendingCopyRepository.delete(locationPendingCopy);
        res.entity = locationPendingCopy;
        res.entityDTO = locationPendingCopyMapper.toDto(locationPendingCopy);
        return res;
    }

    public Result<LocationPendingCopy, LocationPendingCopyReadDTO> getUserPendingLocationsByUsername(String username) {
        UserDTO user = userService.loadWholeUserByUsername(username);

        if (user == null){
            return Result.failure(UserError.notFound(username));
        }

        final UUID currentUserId = user.id();

        List<LocationPendingCopy> entities = locationPendingCopyRepository.getLocationPendingCopiesByUpdatedBy(currentUserId);
        Result<LocationPendingCopy, LocationPendingCopyReadDTO> res = Result.success();
        res.setEntities(entities);
        res.setEntityDTOs(entities.stream().map(locationPendingCopyMapper::toDto).toList());
        return res;
    }
}
