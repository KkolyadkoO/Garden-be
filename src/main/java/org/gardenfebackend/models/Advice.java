package org.gardenfebackend.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
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

    @OneToMany(mappedBy = "advice", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @JsonManagedReference
    private List<AdviceComment> comments = new ArrayList<>();
}

