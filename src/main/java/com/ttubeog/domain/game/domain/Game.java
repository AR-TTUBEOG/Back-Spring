package com.ttubeog.domain.game.domain;

import com.ttubeog.domain.benefit.domain.Benefit;
import com.ttubeog.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "game")
public abstract class Game extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private GameType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "benefit_id")
    private Benefit benefit;

    @Builder
    public Game(Long id, GameType type, Benefit benefit) {
        this.id = id;
        this.type = type;
        this.benefit = benefit;
    }
}
