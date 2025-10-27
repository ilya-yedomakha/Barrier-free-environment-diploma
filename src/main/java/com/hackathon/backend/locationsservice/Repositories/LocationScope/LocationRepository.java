package com.hackathon.backend.locationsservice.Repositories.LocationScope;

import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.Location;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {

    List<Location> findAllByName(String name);

    @Query(value = """
    SELECT *
    FROM locations l
    WHERE ST_DWithin(
              l.coordinates,
              ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography,
              100
          )
      AND (
          similarity(LOWER(l.name), LOWER(:name)) > 0.3
          OR similarity(LOWER(l.address), LOWER(:address)) > 0.3
      )
    ORDER BY (
        similarity(LOWER(l.name), LOWER(:name))
        + similarity(LOWER(l.address), LOWER(:address))
    ) DESC
    LIMIT 20
    """, nativeQuery = true)
    List<Location> findNearbySimilarLocations(
            @Param("lat") double lat,
            @Param("lng") double lng,
            @Param("name") String name,
            @Param("address") String address
    );

    List<Location> findAllByCreatedBy(UUID userId);

    List<Location> findAllByUpdatedBy(UUID userId);
}
