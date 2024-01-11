package com.ttubeog.domain.benefit.domain.repository;

import com.ttubeog.domain.benefit.domain.Benefit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BenefitRepository extends JpaRepository<Benefit, Long> {
}
