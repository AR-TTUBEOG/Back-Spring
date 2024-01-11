package com.ttubeog.domain.spot.domain.repository;

import com.ttubeog.domain.spot.domain.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpotRepository extends JpaRepository<Spot, Long> {
}
