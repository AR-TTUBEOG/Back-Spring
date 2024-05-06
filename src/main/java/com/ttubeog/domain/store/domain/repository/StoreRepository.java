package com.ttubeog.domain.store.domain.repository;

import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.domain.store.domain.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByIdAndMember(Long id, Member member);

    Page<Store> findAllByMemberId(Long memberId, PageRequest pageRequest);
}
