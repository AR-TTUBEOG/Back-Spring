package com.ttubeog.domain.game.domain;

import com.ttubeog.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "basketball_game")
public class BasketballGame extends BaseEntity {

    @Id
    @OneToOne
    @MapsId
    @JoinColumn(name = "game_id")
    private Game game;

    private LocalDateTime timeLimit;

    private Integer ballCount;

    private Integer successCount;

    @Builder
    public BasketballGame(Game game, LocalDateTime timeLimit, Integer ballCount, Integer successCount) {
        this.game = game;
        this.timeLimit = timeLimit;
        this.ballCount = ballCount;
        this.successCount = successCount;
    }
}
