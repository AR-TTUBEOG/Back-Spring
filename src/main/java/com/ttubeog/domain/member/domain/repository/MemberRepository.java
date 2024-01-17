package com.ttubeog.domain.member.domain.repository;

import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.domain.member.dto.MemberDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long>{

    Optional<Member> findByEmail(String email);

    Optional<Member> findByRefreshToken(String refreshToken);

    Boolean existsByEmail(String email);
}
