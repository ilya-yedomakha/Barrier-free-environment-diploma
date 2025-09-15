package com.hackathon.backend.locationsservice.Repositories.LocationScope;

import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.LocationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LocationTypeRepository extends JpaRepository<LocationType, UUID> {
    List<LocationType> findAllByName(String name);
}