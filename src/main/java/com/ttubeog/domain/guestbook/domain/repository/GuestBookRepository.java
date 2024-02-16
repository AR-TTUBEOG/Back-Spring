package com.ttubeog.domain.guestbook.domain.repository;

import com.ttubeog.domain.guestbook.domain.GuestBook;
import com.ttubeog.domain.spot.domain.Spot;
import com.ttubeog.domain.store.domain.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuestBookRepository extends JpaRepository<GuestBook, Long> {

    List<GuestBook> findAllBySpot(Store store);

    Page<GuestBook> findAllBySpot(Spot spot, PageRequest pageRequest);

    Long countAllBySpot(Spot spot);

    @Query("SELECT SUM(g.star) FROM GuestBook g WHERE g.spot.id = :spotId")
    Float sumStarBySpotId(@Param("spotId") Long spotId);

    List<GuestBook> findAllByStore_Id(Long storeId);

    Page<GuestBook> findAllByStore(Store store, PageRequest pageRequest);

    Long countAllByStore(Store store);

    @Query("SELECT SUM(g.star) FROM GuestBook g WHERE g.store.id = :storeId")
    Float sumStarByStoreId(@Param("storeId") Long storeId);

}
