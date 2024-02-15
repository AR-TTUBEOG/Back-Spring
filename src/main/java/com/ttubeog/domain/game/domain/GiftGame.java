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
    private Integer timeLimit;

    @Column(name = "gift_count")
    private Integer giftCount;

    @Builder
    public GiftGame(Game game, int timeLimit, Integer giftCount) {
        this.game = game;
        this.timeLimit = timeLimit;
        this.giftCount = giftCount;
    }

    public void updateTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public void updateGiftCount(Integer giftCount) {
        this.giftCount = giftCount;
    }
}
