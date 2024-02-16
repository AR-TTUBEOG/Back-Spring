package com.ttubeog.domain.likes.domain.repository;

import com.ttubeog.domain.likes.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
  
    List<Likes> findByStore_Id(Long storeId);

    Integer countByStore_Id(Long storeId);
  
    boolean existsByMember_IdAndStore_Id(Long memberId, Long StoreId);
  
    boolean existsByMember_IdAndSpot_Id(Long memberId, Long spotId);

}
