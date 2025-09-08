package com.hackathon.backend.locationsservice.Domain.Core;

import com.hackathon.backend.locationsservice.Domain.Enums.ModerationStatusEnum;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "photos")
public class Photo {

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    private UUID locationId;

    private UUID featureId;

    @Column(length = 500,nullable = false)
    @NotNull
    private String url;

    @Column(length = 500,nullable = false)
    @NotNull
    private String thumbnailUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    private UUID createdBy;

    @Column(name = "moderation_status", nullable = false)
    private ModerationStatusEnum moderationStatusEnum;

    @Min(0)
    @Max(1)
    private Float aiModerationScore;

    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String,Object> aiAccessibilityDetection;
    
    @NotNull
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String rejectReason;

    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String,Object>  metadata;


}
