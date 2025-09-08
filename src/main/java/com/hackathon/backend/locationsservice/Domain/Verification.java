package com.hackathon.backend.locationsservice.Domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jdk.jfr.SettingDefinition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "verifications")
public class Verification {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    @NotNull
    private UUID locationId;

    private UUID featureId;

    @Column(nullable = false)
    @NotNull
    private UUID verifiedId;

    private UUID organizationId;

    @Column(nullable = false)
    @NotNull
    private Boolean status;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String comment;

    private UUID evidencePhotoId;

    @NotNull
    private LocalDateTime createAt = LocalDateTime.now();

    @NotNull
    private Boolean isOfficial = false;
}
