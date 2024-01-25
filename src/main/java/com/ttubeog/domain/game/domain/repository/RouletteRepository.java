package com.ttubeog.domain.game.domain.repository;

import com.ttubeog.domain.game.domain.RouletteGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouletteRepository extends JpaRepository<RouletteGame, Long>{
}
