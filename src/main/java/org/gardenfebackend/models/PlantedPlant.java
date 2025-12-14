package org.gardenfebackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "planted_plants", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"garden_id", "x", "y"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantedPlant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garden_id", nullable = false)
    private Garden garden;

    @Column(nullable = false)
    private Integer x;

    @Column(nullable = false)
    private Integer y;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id", nullable = false)
    private Plant plant;

    @Column(nullable = false)
    private LocalDate plantedDate;
}

