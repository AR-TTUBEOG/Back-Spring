package com.ttubeog.domain.roadcoordinate.domain.repository;

import com.ttubeog.domain.road.domain.Road;
import com.ttubeog.domain.roadcoordinate.domain.RoadCoordinate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoadCoordinateRepository extends JpaRepository<RoadCoordinate, Long> {

    List<RoadCoordinate> findByRoad(Road road);

}
