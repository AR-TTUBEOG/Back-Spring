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
    @Column(name = "game_id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "game_id")
    private Game game;

    @ElementCollection
    @Column(name = "options")
    private List<String> options;

    @Builder
    public RouletteGame(Game game, List<String> options) {
        this.game = game;
        this.options = options;
    }

    public void updateRoulette(List<String> options) {
        this.options = options;
    }
}
