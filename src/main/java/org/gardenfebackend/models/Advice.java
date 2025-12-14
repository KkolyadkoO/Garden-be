package org.gardenfebackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "advices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Advice {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String photoUrl;

    @Column(columnDefinition = "text", nullable = false)
    private String description;
}

