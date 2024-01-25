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
@Table(name = "roulette_option")
public class RouletteOption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roulette_game_id")
    private RouletteGame rouletteGame;

    private String content;

    @Builder
    public RouletteOption(Long optionId, RouletteGame rouletteGame, String content) {
        this.optionId = optionId;
        this.rouletteGame = rouletteGame;
        this.content = content;
    }
}
