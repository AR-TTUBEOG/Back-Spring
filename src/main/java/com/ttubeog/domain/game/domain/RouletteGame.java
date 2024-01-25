package com.ttubeog.domain.game.domain;

import com.ttubeog.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "roulette_game")
public class RouletteGame extends BaseEntity {

    @Id
    @OneToOne
    @MapsId
    @JoinColumn(name = "game_id")
    private Game game;

    private int optionCount;

    @Builder
    public RouletteGame(Game game, int optionCount) {
        this.game = game;
        this.optionCount = optionCount;
    }
}
