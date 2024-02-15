package com.ttubeog.domain.guestbook.domain.repository;

import com.ttubeog.domain.guestbook.domain.GuestBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface GuestBookRepository extends JpaRepository<GuestBook, Long> {

    List<GuestBook> findAllBySpot_Id(Long spotId);

    Page<GuestBook> findAllBySpot_Id(Long spotId, PageRequest pageRequest);

    Long countAllBySpot_Id(Long spotId);

    @Query("SELECT SUM(g.star) FROM GuestBook g WHERE g.spot.id = :spotId")
    Float sumStarBySpotId(@Param("spotId") Long spotId);

    List<GuestBook> findAllByStore_Id(Long storeId);

    Page<GuestBook> findAllByStore_Id(Long storeId, PageRequest pageRequest);

    Long countAllByStore_Id(Long storeId);

    @Query("SELECT SUM(g.star) FROM GuestBook g WHERE g.store.id = :storeId")
    Float sumStarByStoreId(@Param("storeId") Long storeId);

}
