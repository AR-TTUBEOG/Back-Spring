package com.ttubeog.domain.benefit.domain.repository;

import com.ttubeog.domain.benefit.domain.Benefit;
import com.ttubeog.domain.game.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BenefitRepository extends JpaRepository<Benefit, Long> {

    Optional<Benefit> findByGame(Game game);

    List<Benefit> findAllByGame(Game game);

    List<Benefit> findByStoreId(Long storeId);
}
