package com.ttubeog.domain.benefit.domain.repository;

import com.ttubeog.domain.benefit.domain.Benefit;
import com.ttubeog.domain.benefit.domain.MemberBenefit;
import com.ttubeog.domain.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberBenefitRepository extends JpaRepository<MemberBenefit, Long> {

    Boolean existsByBenefitAndCreatedAtIsAfter(Benefit benefit, LocalDateTime createdAt);

    Optional<MemberBenefit> findByBenefitAndMemberAndExpiredIsFalse(Benefit benefit, Member member);

    List<MemberBenefit> findAllByExpiredFalse();

    Page<MemberBenefit> findAllByMember(Member member, PageRequest pageRequest);
}
