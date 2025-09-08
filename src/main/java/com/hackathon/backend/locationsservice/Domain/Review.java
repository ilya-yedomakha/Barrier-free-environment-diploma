package com.hackathon.backend.locationsservice.Domain;

import com.hackathon.backend.locationsservice.Domain.Enums.ModerationStatusEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "reviews")
public class Review {

        @Id
        @GeneratedValue
        private UUID id;

        @NotNull
        @Column(nullable = false)
        private UUID locationId;

        @NotNull
        @Column(nullable = false)
        private UUID userId;

        @NotNull
        @Min(1)
        @Max(5)
        @Column(nullable = false)
        private Integer rating;

        @NotBlank
        @Column(columnDefinition = "TEXT")
        private String comment;

        @NotBlank
        @Column(columnDefinition = "TEXT")
        private String accessibilityExperience;

        @NotNull
        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private ModerationStatusEnum moderationStatus;

        @NotNull
        @Column(nullable = false)
        private LocalDateTime createdAt = LocalDateTime.now();

        @NotNull
        @Column(nullable = false)
        private LocalDateTime updatedAt = LocalDateTime.now();
}
