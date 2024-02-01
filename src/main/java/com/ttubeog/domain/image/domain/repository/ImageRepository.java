package com.ttubeog.domain.image.domain.repository;

import com.ttubeog.domain.image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Override
    List<Image> findAll();

    <Image> findBySpotId(Long id);
}
