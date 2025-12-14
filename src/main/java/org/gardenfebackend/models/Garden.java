package org.gardenfebackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gardenfebackend.converters.GardenTypeAttributeConverter;
import org.gardenfebackend.enums.GardenType;

import java.util.UUID;

@Entity
@Table(name = "gardens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Garden {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Convert(converter = GardenTypeAttributeConverter.class)
    @Column(nullable = false)
    private GardenType type;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

