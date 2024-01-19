package com.ttubeog.domain.benefit.domain.repository;

import com.ttubeog.domain.benefit.domain.Benefit;
import com.ttubeog.domain.benefit.domain.MemberBenefit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MemberBenefitRepository extends JpaRepository<MemberBenefit, Long> {

    List<MemberBenefit> findAllByBenefitAndCreatedDateIsAfter(Benefit benefit, LocalDateTime createdDate);

}
