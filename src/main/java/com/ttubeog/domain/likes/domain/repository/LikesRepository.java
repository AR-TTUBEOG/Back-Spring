package com.ttubeog.domain.likes.domain.repository;

import com.ttubeog.domain.likes.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
    List<Likes> findByStoreId(Long storeId);

    Integer countByStoreId(Long storeId);

    Boolean existsByMemberIdAndStoreId(Long memberId, Long storeId);
}
