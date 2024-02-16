package com.ttubeog.domain.roadcoordinate.domain.repository;

import com.ttubeog.domain.roadcoordinate.domain.RoadCoordinate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoadCoordinateRepository extends JpaRepository<RoadCoordinate, Long> {
}
