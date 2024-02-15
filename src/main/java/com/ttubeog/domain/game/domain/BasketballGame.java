package com.ttubeog.domain.game.domain;

import com.ttubeog.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "basketball_game")
public class BasketballGame extends BaseEntity {

    @Id
    @Column(name = "game_id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "game_id")
    private Game game;

    @Column(name = "time_limit")
    private Integer timeLimit;

    @Column(name = "ball_count")
    private Integer ballCount;

    @Column(name = "success_count")
    private Integer successCount;

    @Builder
    public BasketballGame(Game game, Integer timeLimit, Integer ballCount, Integer successCount) {
        this.game = game;
        this.timeLimit = timeLimit;
        this.ballCount = ballCount;
        this.successCount = successCount;
    }

    public void updateTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public void updateBallCount(Integer ballCount) {
        this.ballCount = ballCount;
    }

    public void updateSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }
}
