package org.gardenfebackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "user_favorite_advices", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "advice_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFavoriteAdvice {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advice_id", nullable = false)
    private Advice advice;
}

