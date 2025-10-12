package com.hackathon.backend.locationsservice.Repositories.LocationScope;

import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.additional.LocationPendingCopy;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LocationPendingCopyRepository extends JpaRepository<LocationPendingCopy, Long> {
    List<LocationPendingCopy> findAllByLocation_IdAndUpdatedBy(UUID locationId, UUID updatedBy);

    List<LocationPendingCopy> getLocationPendingCopiesByUpdatedBy(@NotNull UUID updatedBy);
}
