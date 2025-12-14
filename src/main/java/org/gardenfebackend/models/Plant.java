package org.gardenfebackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gardenfebackend.enums.PlantType;

import java.util.UUID;

@Entity
@Table(name = "plants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlantType type;

    @Column(nullable = false)
    private boolean isVerified = false;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String photoUrl;

    @Column(nullable = false)
    private String sunHours;

    @Column(nullable = false)
    private String temperature;

    @Column(nullable = false)
    private Integer wateringDays;

    @Column(nullable = false)
    private String goodNeighbours;

    @Column(nullable = false)
    private String badNeighbours;

    @Column(columnDefinition = "text")
    private String description;
}


