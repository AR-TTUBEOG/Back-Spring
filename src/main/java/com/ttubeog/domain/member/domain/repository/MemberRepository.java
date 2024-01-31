package com.ttubeog.domain.member.domain.repository;

import com.ttubeog.domain.auth.domain.Platform;
import com.ttubeog.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long>{

    Optional<Member> findByEmail(String email);
    Optional<Member> findByPlatformAndPlatformId(Platform platform, String platformId);

    Optional<Member> findByRefreshToken(String refreshToken);

    Boolean existsByEmail(String email);

    @Query("select m.id from Member m where m.platform = :platform and m.platformId = :platformId")
    Optional<Long> findIdByPlatformAndPlatformId(Platform platform, String platformId);

}
