package com.ttubeog.domain.benefit.domain.repository;

import com.ttubeog.domain.benefit.domain.MemberBenefit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberBenefitRepository extends JpaRepository<MemberBenefit, Long> {
}
