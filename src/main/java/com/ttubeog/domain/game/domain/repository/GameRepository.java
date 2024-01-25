package com.ttubeog.domain.game.domain.repository;

import com.ttubeog.domain.benefit.domain.Benefit;
import com.ttubeog.domain.game.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    Boolean existsByBenefit(Benefit benefit);
}
