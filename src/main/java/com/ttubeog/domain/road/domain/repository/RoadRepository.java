package com.ttubeog.domain.road.domain.repository;

import com.ttubeog.domain.road.domain.Road;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoadRepository extends JpaRepository<Road, Long> {
}
