package com.ttubeog.domain.road.domain.repository;

import com.ttubeog.domain.road.domain.Road;
import com.ttubeog.domain.spot.domain.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoadRepository extends JpaRepository<Road, Long> {

    Optional<Road> findBySpotAndName(Spot spot, String name);
}
