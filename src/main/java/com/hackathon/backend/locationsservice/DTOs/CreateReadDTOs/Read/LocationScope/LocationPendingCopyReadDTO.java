package com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.LocationScope;

import com.hackathon.backend.locationsservice.Domain.Enums.LocationStatusEnum;
import com.hackathon.backend.locationsservice.Domain.JSONB_POJOs.Contacts;
import com.hackathon.backend.locationsservice.Domain.JSONB_POJOs.WorkingHours;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationPendingCopyReadDTO {

    private Long id;

    private UUID locationId;

    private String name;

    private String address;

    private String description;

    private Contacts contacts;

    private WorkingHours workingHours;

    private UUID organizationId;

    private LocationStatusEnum status;

    private LocalDateTime updatedAt;

    private UUID updatedBy;
}
