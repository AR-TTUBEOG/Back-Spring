package com.ttubeog.domain.image.domain.repository;

import com.ttubeog.domain.guestbook.domain.GuestBook;
import com.ttubeog.domain.image.domain.Image;
import com.ttubeog.domain.spot.domain.Spot;
import com.ttubeog.domain.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    @Override
    List<Image> findAll();

    List<Image> findBySpotId(Long id);

    List<Image> findByStoreId(Long id);

    Optional<Image> findByGuestBookId(Long id);

    List<Image> findAllBySpot(Spot spot);

    List<Image> findAllByStore(Store store);

    List<Image> findAllByGuestBook(GuestBook guestBook);

}
