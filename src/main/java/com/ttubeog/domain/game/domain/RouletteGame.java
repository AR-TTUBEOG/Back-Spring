package com.ttubeog.domain.game.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "roulette_game")
public class RouletteGame {

    @Id
    @OneToOne
    @MapsId
    @JoinColumn(name = "game_id")
    private Game game;

    private int optionCount;

    // 연관 관계 매핑
    @OneToMany(mappedBy = "rouletteGame")
    private List<RouletteOption> options;

}
