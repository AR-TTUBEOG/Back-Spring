package com.ttubeog.domain.area.domain.repository;

import com.ttubeog.domain.area.domain.DongArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DongAreaRepository extends JpaRepository<DongArea, Long> {
}
