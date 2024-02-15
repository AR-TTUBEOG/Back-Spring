package com.ttubeog.domain.guestbook.domain.repository;

import com.ttubeog.domain.guestbook.domain.GuestBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuestBookRepository extends JpaRepository<GuestBook, Long> {

    List<GuestBook> findAllBySpot_Id(Long spotId);

    Long countAllBySpot_Id(Long spotId);

    @Query("SELECT SUM(g.star) FROM GuestBook g WHERE g.spot.id = :spotId")
    Float sumStarBySpotId(@Param("spotId") Long spotId);

    List<GuestBook> findAllByStore_Id(Long storeId);

    Long countAllByStore_Id(Long storeId);

    @Query("SELECT SUM(g.star) FROM GuestBook g WHERE g.store.id = :storeId")
    Float sumStarByStoreId(@Param("storeId") Long storeId);

    List<GuestBook> findByStoreId(Long storeId);

    Integer countByStoreId(Long storeId);
}
