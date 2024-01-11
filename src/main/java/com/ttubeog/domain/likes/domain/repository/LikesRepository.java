package com.ttubeog.domain.likes.domain.repository;

import com.ttubeog.domain.likes.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
}
