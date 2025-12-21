package org.gardenfebackend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "support_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String subject;

    @Column(columnDefinition = "text", nullable = false)
    private String message;

    @Column(nullable = false)
    private Instant createdAt;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
    }
}

