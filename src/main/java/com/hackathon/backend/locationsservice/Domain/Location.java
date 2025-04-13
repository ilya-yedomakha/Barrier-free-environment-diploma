package com.hackathon.backend.locationsservice.Domain;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hackathon.backend.locationsservice.Domain.Enums.LocationStatusEnum;
import com.hackathon.backend.locationsservice.Domain.Enums.LocationTypeEnum;
import com.hackathon.backend.locationsservice.Domain.JSONB_POJOs.Contacts;
import com.hackathon.backend.locationsservice.Domain.JSONB_POJOs.WorkingHours;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

import org.locationtech.jts.geom.Point;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @NotBlank
    @Column(length = 255, nullable = false)
    private String name;

    @NotNull
    @NotBlank
    @Column(length = 500, nullable = false)
    private String address;

    @NotNull
    @JsonDeserialize(using = PointDeserializer.class)
    @Column(columnDefinition = "geometry(Point,4326)", nullable = false)
    private Point coordinates;


    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private LocationTypeEnum type;

    @Column(length = 100)
    private String category;

    @Column(columnDefinition = "TEXT")
    private String description;


    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "phone", column = @Column(name = "contacts_phone")),
            @AttributeOverride(name = "email", column = @Column(name = "contacts_email")),
            @AttributeOverride(name = "website", column = @Column(name = "contacts_website"))
    })

    @Column(columnDefinition = "jsonb")
    private String contacts;

    @Column(columnDefinition = "jsonb")
    private String workingHours;

    @NotNull
    @Column(nullable = false)
    private UUID createdBy;

    @Column(name = "organization_id")
    private UUID organizationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LocationStatusEnum status;

    @Column(name = "overall_accessibility_score")
    private Integer overallAccessibilityScore;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @NotNull
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @NotNull
    @Column(nullable = false)
    private LocalDateTime lastVerifiedAt;

    @Column(columnDefinition = "TEXT")
    private String rejectionReason;

    static class PointDeserializer extends JsonDeserializer<Point> {

        private final GeometryFactory geometryFactory = new GeometryFactory();

        @Override
        public Point deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, IOException {
            JsonNode node = p.getCodec().readTree(p);
            double lat = node.get("lat").asDouble();
            double lng = node.get("lng").asDouble();
            return geometryFactory.createPoint(new Coordinate(lng, lat));
        }
    }

}

