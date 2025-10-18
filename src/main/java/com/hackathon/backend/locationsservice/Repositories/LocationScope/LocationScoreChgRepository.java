package com.hackathon.backend.locationsservice.Repositories.LocationScope;

import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.Location;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.additional.LocationScoreChg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LocationScoreChgRepository extends JpaRepository<LocationScoreChg, UUID> {

    @Query("SELECT l.locationId FROM LocationScoreChg l")
    List<UUID> getLocationScoreChg_locationId();
}
