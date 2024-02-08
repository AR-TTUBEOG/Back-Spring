package com.ttubeog.domain.benefit.domain;

import com.ttubeog.domain.common.BaseEntity;
import com.ttubeog.domain.game.domain.Game;
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

//    @OneToMany(mappedBy = "benefit", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Game> game;

    @Builder
    public Benefit(String content, BenefitType type, Game game) {
        this.content = content;
        this.type = type;
        this.game = game;
    }

    public void updateBenefit(String content, BenefitType type) {
        this.content = content;
        this.type = type;
    }

    public void deleteGame() {
        this.game = null;
    }
}
