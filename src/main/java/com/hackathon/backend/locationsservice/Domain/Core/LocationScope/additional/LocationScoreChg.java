package com.hackathon.backend.locationsservice.Domain.Core.LocationScope.additional;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationScoreChg {

    @Id
    private UUID locationId;
}
