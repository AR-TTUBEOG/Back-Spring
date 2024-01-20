package com.ttubeog.domain.game.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "basketball_game")
public class BasketballGame {

    @Id
    @OneToOne
    @MapsId
    @JoinColumn(name = "game_id")
    private Game game;

    private LocalDateTime timeLimit;

    private Integer ballCount;

    private Integer successCount;
}
