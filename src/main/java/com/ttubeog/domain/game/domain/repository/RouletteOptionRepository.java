package com.ttubeog.domain.game.domain.repository;

import com.ttubeog.domain.game.domain.RouletteOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouletteOptionRepository extends JpaRepository<RouletteOption, Long> {
}
