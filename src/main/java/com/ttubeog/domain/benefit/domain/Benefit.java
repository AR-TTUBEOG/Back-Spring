package com.ttubeog.domain.benefit.domain;

import com.ttubeog.domain.common.BaseEntity;
import com.ttubeog.domain.game.domain.Game;
import com.ttubeog.domain.store.domain.Store;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "benefit")
public class Benefit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private BenefitType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Builder
    public Benefit(String content, BenefitType type, Game game, Store store) {
        this.content = content;
        this.type = type;
        this.game = game;
        this.store = store;
    }

    public void deleteGame() {
        this.game = null;
    }
}
