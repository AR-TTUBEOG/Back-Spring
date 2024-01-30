package com.ttubeog.domain.image.domain.repository;

import com.ttubeog.domain.image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
