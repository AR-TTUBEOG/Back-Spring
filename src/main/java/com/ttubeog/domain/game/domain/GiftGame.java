package com.ttubeog.domain.game.domain;

import com.ttubeog.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "gift_game")
public class GiftGame extends BaseEntity {

    @Id
    @Column(name = "game_id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "game_id")
    private Game game;

    @Column(name = "time_limit")
    private LocalTime timeLimit;

    @Column(name = "gift_count")
    private Integer giftCount;

    @Builder
    public GiftGame(Game game, LocalTime timeLimit, Integer giftCount) {
        this.game = game;
        this.timeLimit = timeLimit;
        this.giftCount = giftCount;
    }
}
