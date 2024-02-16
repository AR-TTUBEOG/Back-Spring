package com.ttubeog.domain.store.domain.repository;

import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.domain.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByIdAndMember(Long id, Member member);
}
