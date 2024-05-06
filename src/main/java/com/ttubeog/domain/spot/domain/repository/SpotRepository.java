package com.ttubeog.domain.spot.domain.repository;

import com.ttubeog.domain.spot.domain.Spot;
import com.ttubeog.domain.store.domain.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpotRepository extends JpaRepository<Spot, Long> {

    Optional<Spot> findByName(String name);
    Page<Spot> findAllByMemberId(Long memberId, PageRequest pageRequest);
}
