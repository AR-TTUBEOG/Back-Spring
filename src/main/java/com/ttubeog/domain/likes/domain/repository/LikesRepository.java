package com.ttubeog.domain.likes.domain.repository;

import com.ttubeog.domain.likes.domain.Likes;
import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.domain.spot.domain.Spot;
import com.ttubeog.domain.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
  
    List<Likes> findByStore(Store store);

    Integer countByStore(Store store);

    List<Likes> findBySpot(Spot spot);

    Integer countBySpot(Spot spot);
  
    boolean existsByMemberAndStore(Member member, Store store);
  
    boolean existsByMemberAndSpot(Member member, Spot spot);

}
