package com.ttubeog.domain.road.domain.repository;

import com.ttubeog.domain.road.domain.Road;
import com.ttubeog.domain.spot.domain.Spot;
import com.ttubeog.domain.store.domain.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoadRepository extends JpaRepository<Road, Long> {

    Optional<Road> findBySpotAndName(Spot spot, String name);

    Optional<Road> findByStoreAndName(Store store, String name);

    Page<Road> findAllBySpot_Id(Long spotId, PageRequest pageRequest);

    Page<Road> findAllByStore_Id(Long storeId, PageRequest pageRequest);

}
