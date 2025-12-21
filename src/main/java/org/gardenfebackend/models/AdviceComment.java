package org.gardenfebackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "advice_comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdviceComment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(columnDefinition = "text", nullable = false)
    private String text;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "advice_id", nullable = false)
    @JsonBackReference
    private Advice advice;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    public void onCreate() {
        createdAt = Instant.now();
    }
}
