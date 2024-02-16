package com.ttubeog.domain.image.domain.repository;

import com.ttubeog.domain.image.domain.Image;
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
}
