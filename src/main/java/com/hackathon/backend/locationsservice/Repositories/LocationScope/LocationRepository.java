package com.hackathon.backend.locationsservice.Repositories.LocationScope;

import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {

}
