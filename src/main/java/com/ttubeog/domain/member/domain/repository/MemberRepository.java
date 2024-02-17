package com.ttubeog.domain.member.domain.repository;

import com.ttubeog.domain.auth.domain.Platform;
import com.ttubeog.domain.auth.domain.Status;
import com.ttubeog.domain.member.domain.Member;
import com.ttubeog.domain.spot.domain.Spot;
import com.ttubeog.domain.store.domain.Store;
import io.lettuce.core.dynamic.annotation.Param;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long>{

    @NotNull
    Optional<Member> findById(@NotNull Long userId);

    Optional<Member> findByEmail(String email);
    Optional<Member> findByMemberNumber(String memberNumber);

    List<Member> findByStatus(Status status);

    Optional<Member> findByPlatformAndPlatformId(Platform platform, String platformId);

    Optional<Member> findByRefreshToken(String refreshToken);

    Boolean existsByEmail(String email);

    @Query("select m.id from Member m where m.platform = :platform and m.platformId = :platformId")
    Optional<Long> findIdByPlatformAndPlatformId(Platform platform, String platformId);

    @Modifying
    @Query("update Member as m set m.nickname = :nickName where m.id = :memberId")
    void updateMemberNickname(@Param("nickName") String nickName, @Param("memberId") Long memberId);

    @Modifying
    @Query("update Member as m set m.nicknameChange = :nicknameChange where m.id = :memberId")
    void updateMemberNicknameChange(@Param("nicknameChange") Integer nicknameChange, @Param("memberId") Long memberId);


    Boolean existsByNickname(String nickname);

    @Modifying
    @Query("SELECT s FROM Spot s WHERE s.member.id = :memberId")
    List<Spot> findSpotByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Query("SELECT s FROM Store s WHERE s.member.id = :memberId")
    List<Store> findStoreByMemberId(@Param("memberId") Long memberId);
}
