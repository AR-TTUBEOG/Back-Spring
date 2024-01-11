package com.ttubeog.domain.area.domain.repository;

import com.ttubeog.domain.area.domain.SiggArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiggAreaRepository extends JpaRepository<SiggArea, Long> {
}
