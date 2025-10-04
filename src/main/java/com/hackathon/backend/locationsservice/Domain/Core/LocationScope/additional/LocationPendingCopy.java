package com.hackathon.backend.locationsservice.Domain.Core.LocationScope.additional;

import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.Location;
import com.hackathon.backend.locationsservice.Domain.Enums.LocationStatusEnum;
import com.hackathon.backend.locationsservice.Domain.JSONB_POJOs.Contacts;
import com.hackathon.backend.locationsservice.Domain.JSONB_POJOs.WorkingHours;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

@Entity
@Table(name = "Location_pending_copies")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationPendingCopy{

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id", nullable = false)
    private Location location;

    @NotNull
    @NotBlank
    @Column(length = 255, nullable = false)
    private String name;

    @NotNull
    @NotBlank
    @Column(length = 500, nullable = false)
    private String address;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "contacts", columnDefinition = "jsonb")
    private Contacts contacts;

    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "working_hours", columnDefinition = "jsonb")
    private WorkingHours workingHours;


    @Column(name = "organization_id")
    private UUID organizationId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LocationStatusEnum status = LocationStatusEnum.pending;


    @NotNull
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}
