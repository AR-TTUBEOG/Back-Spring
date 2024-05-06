package com.ttubeog.domain.game.domain.repository;

import com.ttubeog.domain.game.domain.BasketballGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketBallRepository extends JpaRepository<BasketballGame, Long> {
}
