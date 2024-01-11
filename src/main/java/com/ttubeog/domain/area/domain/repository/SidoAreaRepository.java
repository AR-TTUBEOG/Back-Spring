package com.ttubeog.domain.area.domain.repository;

import com.ttubeog.domain.area.domain.SidoArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SidoAreaRepository extends JpaRepository<SidoArea, Long> {
}
