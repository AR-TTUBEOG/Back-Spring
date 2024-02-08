package com.ttubeog.domain.game.domain.repository;

import com.ttubeog.domain.game.domain.GiftGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftGameRepository extends JpaRepository<GiftGame, Long> {
}
