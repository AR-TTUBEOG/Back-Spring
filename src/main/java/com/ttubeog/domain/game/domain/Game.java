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
public class Game extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private GameType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "benefit_id")
    private Benefit benefit;

    @OneToOne(mappedBy = "game", cascade = CascadeType.ALL)
    private GiftGame giftGame;

    @OneToOne(mappedBy = "game", cascade = CascadeType.ALL)
    private BasketballGame basketballGame;

    @OneToOne(mappedBy = "game", cascade = CascadeType.ALL)
    private RouletteGame rouletteGame;


    @Builder
    public Game(GameType type, Benefit benefit) {
        this.type = type;
        this.benefit = benefit;
    }
}
