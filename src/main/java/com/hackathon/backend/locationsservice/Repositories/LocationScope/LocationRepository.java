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

    @Query(value = """
        SELECT * FROM locations l
        WHERE ST_DWithin(l.coordinates, ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography, 50)
        """, nativeQuery = true)
    List<Location> findNearby(@Param("lat") double lat, @Param("lng") double lng);
    List<Location> findAllByName(String name);
}
