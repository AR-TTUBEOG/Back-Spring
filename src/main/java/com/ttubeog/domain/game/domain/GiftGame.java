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
@Table(name = "gift_game")
public class GiftGame extends BaseEntity {

    @Id
    @OneToOne
    @MapsId
    @JoinColumn(name = "game_id")
    private Game game;

    private LocalDateTime timeLimit;

    private Integer giftCount;

    @Builder
    public GiftGame(Game game, LocalDateTime timeLimit, Integer giftCount) {
        this.game = game;
        this.timeLimit = timeLimit;
        this.giftCount = giftCount;
    }
}
